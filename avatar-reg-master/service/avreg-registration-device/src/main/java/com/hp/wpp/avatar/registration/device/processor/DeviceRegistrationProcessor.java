package com.hp.wpp.avatar.registration.device.processor;

import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.wpp.avatar.framework.common.config.RegistrationProcessorConfig;
import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.enums.ServiceInstanceType;
import com.hp.wpp.avatar.framework.eventnotification.schema.DeRegisterEventNotificationMessage;
import com.hp.wpp.avatar.framework.eventnotification.schema.EventDetails;
import com.hp.wpp.avatar.framework.eventnotification.schema.EventOriginator;
import com.hp.wpp.avatar.framework.exceptions.EmptyConfigUrlException;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.InvalidEntityDomainIndexException;
import com.hp.wpp.avatar.framework.exceptions.InvalidRegistrationDataException;
import com.hp.wpp.avatar.framework.exceptions.ModellingException;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.framework.processor.data.LinkBO;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.registration.device.entities.DeviceAdditionalInfo;
import com.hp.wpp.avatar.registration.device.entities.EntityDevice;
import com.hp.wpp.avatar.registration.device.entities.HashedEntityIdentifier;
import com.hp.wpp.avatar.registration.device.entities.ServiceInstance;
import com.hp.wpp.avatar.registration.device.entities.domain.RegistrationDomain;
import com.hp.wpp.avatar.registration.device.repository.DeviceServiceInstanceRepository;
import com.hp.wpp.avatar.registration.device.repository.EntityDeviceRepository;
import com.hp.wpp.avatar.registration.device.repository.HashedEntityIdentifierRepository;
import com.hp.wpp.avatar.registration.device.repository.RegistrationDomainRepository;
import com.hp.wpp.avatar.registration.device.schema.DeviceClaim;
import com.hp.wpp.cidgenerator.CloudIdGenerator;
import com.hp.wpp.http.WppHttpClient;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.ssn.SignedSerialNumberValidator;
import com.hp.wpp.stream.messages.client.EventProducer;

public class DeviceRegistrationProcessor implements EntityRegistrationProcessor {

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(DeviceRegistrationProcessor.class);

    private static final String SHA256_ALGORITHM_NAME = "SHA-256";

    private static final String DLS_VERSION = "1.0";

    @Autowired
    SignedSerialNumberValidator ssnValidator;

    @Autowired
    EntityDeviceRepository entityDeviceRepository;

    @Autowired
    HashedEntityIdentifierRepository hashedEntityIdentifierRepository;

    @Autowired
    private RegistrationDomainRepository registrationDomainRepository;

    @Autowired
    private CloudIdGenerator cloudIdGenerator;

    @Autowired
    private DeviceServiceInstanceRepository deviceServiceInstanceRepository;

    @Autowired
    private EventProducer eventProducer;

    private short podCode;

    private DeviceClaimHeaders deviceClaimHeaders;

    private String deRegistrationStreamName;

    private static final String SERVICE_NAME = "avatar-reg";
    private static String EVENT_VERSION = "1.0.0";

    //private boolean ssnEnabled;

    public String getDeRegistrationStreamName() {
        return deRegistrationStreamName;
    }

    public void setDeRegistrationStreamName(String deRegistrationStreamName) {
        this.deRegistrationStreamName = deRegistrationStreamName;
    }

    public DeviceClaimHeaders getDeviceClaimHeaders() {
        return deviceClaimHeaders;
    }

    public void setDeviceClaimHeaders(DeviceClaimHeaders deviceClaimHeaders) {
        this.deviceClaimHeaders = deviceClaimHeaders;
    }

    public void setEventProducer(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @Autowired
    private RegistrationProcessorConfig registrationProcessorConfig;

    private ThreadPoolExecutor threadPoolExecutor;
    private WppHttpClient deviceClaimHttpClient;

    public DeviceRegistrationProcessor(WppHttpClient wppHttpClient, ThreadPoolExecutor threadPoolExecutor) {
        this.deviceClaimHttpClient = wppHttpClient;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void setRegistrationProcessorConfig(RegistrationProcessorConfig registrationProcessorConfig) {
        this.registrationProcessorConfig = registrationProcessorConfig;
    }

    public short getPodCode() {
        return podCode;
    }

    public void setPodCode(short podCode) {
        this.podCode = podCode;
    }

	/*public boolean isSsnEnabled() {
        return ssnEnabled;
	}

	public void setSsnEnabled(boolean ssnEnabled) {
		this.ssnEnabled = ssnEnabled;
	}*/

    public void setEntityDeviceRepository(
            EntityDeviceRepository entityDeviceRepository) {
        this.entityDeviceRepository = entityDeviceRepository;
    }

    public void setHashedEntityIdentifierRepository(HashedEntityIdentifierRepository hashedEntityIdentifierRepository) {
        this.hashedEntityIdentifierRepository = hashedEntityIdentifierRepository;
    }

    public void setRegistrationDomainRepository(RegistrationDomainRepository registrationDomainRepository) {
        this.registrationDomainRepository = registrationDomainRepository;
    }

    public void setDeviceServiceInstanceRepository(DeviceServiceInstanceRepository serviceInstanceRepository) {
        this.deviceServiceInstanceRepository = serviceInstanceRepository;
    }

    public SignedSerialNumberValidator getSsnValidator() {
        return ssnValidator;
    }

    public void setSsnValidator(SignedSerialNumberValidator ssnValidator) {
        this.ssnValidator = ssnValidator;
    }

    public void setCloudIdGenerator(CloudIdGenerator cloudIdGenerator) {
        this.cloudIdGenerator = cloudIdGenerator;
    }

    @Override
    public EntityConfigurationBO registerEntity(EntityIdentificationBO entityIdentification) throws EntityRegistrationNonRetriableException {

        DeviceClaim claim = new DeviceClaim();
        claim.setVersion(DLS_VERSION);
        claim.setEntityId(entityIdentification.getEntityId());

		/*if(isSsnEnabled()) {
            try {
				ssnValidator.validateAndPersistSSN(entityIdentification.getSSN(), entityIdentification.getEntityId());
			} catch (InvalidSSNException e) {
				LOG.error("SSN [{}] validation failed with error message: {}", entityIdentification.getSSN(), e.getMessage());
				throw new InvalidRegistrationDataException(e);
			}
		}*/

        /** First Check Domain is  vaild
         *
         */
        RegistrationDomain domain = registrationDomainRepository.findByRegistrationDomain(entityIdentification.getEntityDomain());
        if (domain == null) {
            throw new InvalidEntityDomainIndexException("Invalid domain index");
        }
        HashedEntityIdentifier hashedSerial = generateHashedSerialNumber(entityIdentification.getEntityId());
        String hashedSerialNumber = hashedSerial.getHashedEntityIdentifier();
        EntityDevice device = createEntityDevice(entityIdentification);
        device.setEntityId(hashedSerialNumber);
        EntityDevice deviceDB = entityDeviceRepository.findByEntityIdAndEntityModel(hashedSerialNumber, device.getEntityModel());
        if (deviceDB == null) {
            createDevice(device, hashedSerial);
            //Removing the async call to dls. DLS will read from reg stream
            //LOG.debug("Device Claim getting updated with fresh registration for cloud_id: {}",device.getCloudId());
            //updateDeviceClaim(domain, claim, device);
        } else {
            if (device.getResetCounter() == deviceDB.getResetCounter()) {
                LOG.debug("Device is being updated");
                updateOnReRegister(deviceDB, device);
            } else {
                String oldCloudId = deviceDB.getCloudId();
                LOG.debug("Device is getting re-registered for entity_id: {}", entityIdentification.getEntityId());
                reRegisterDevice(deviceDB, device);
                //Removing the async call to dls. DLS will read from reg stream
                /*LOG.debug("Device claim is getting updated after re-registration for cloud_id: {}", deviceDB.getCloudId());
                updateDeviceClaim(domain, claim, device);*/
                LOG.debug("Notify de-registration event for old_cloud_id={}, updated with new cloud_id={}", oldCloudId, device.getCloudId());
                notifyDeRegistrationEvent(oldCloudId);
            }
        }
        //post registration operations
        entityIdentification.setPodCode(getPodCode());
        return createDeviceConfiguration(device, domain.getDomainIndex());
    }

    private void notifyDeRegistrationEvent(String cloudId) {

        try {

            DeRegisterEventNotificationMessage deRegisterEventNotificationMessage = new DeRegisterEventNotificationMessage();

            deRegisterEventNotificationMessage.setVersion(EVENT_VERSION);
            deRegisterEventNotificationMessage.setSubscriptionId("sub" + cloudId);
            deRegisterEventNotificationMessage.setEventOriginator(buildEventOriginator());

            EventDetails eventDetails = new EventDetails();
            eventDetails.setEventName("De-Registration");
            eventDetails.setEntityId(cloudId);
            deRegisterEventNotificationMessage.setEventDetails(eventDetails);

            eventProducer.sendNotification(deRegistrationStreamName, deRegisterEventNotificationMessage.toJsonAsBytes());
            LOG.debug("Notified de-registration event for cloud_id={}", cloudId);
        } catch (Exception ex) {

            LOG.error("api=notifyDeRegistrationEvent executionType=SyncFlow; cloudId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId, ex.getMessage());

        }
    }

    private EventOriginator buildEventOriginator() {
        EventOriginator eventOriginator = new EventOriginator();
        eventOriginator.setSource(SERVICE_NAME);
        eventOriginator.setTimeOfCreation(new Date().toString());
        return eventOriginator;
    }

    private void updateDeviceClaim(RegistrationDomain domain, DeviceClaim claim, EntityDevice device) {
        String cloudId = device.getCloudId();
        try {
            claim.setCloudId(cloudId);
            claim.setDomainIndex(domain.getDomainIndex());
            threadPoolExecutor.execute(new DeviceClaimProcessor(deviceClaimHttpClient, claim, getDeviceClaimHeaders()));
        } catch (Exception e) {
            //TODO retry logic required.
            if (e instanceof InvalidEntityDomainIndexException)
                throw e;
            else
                LOG.error("api=updateDLS method=PUT executionType=ASyncFlow; cloudId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId, e.getMessage());
        }
    }


    private EntityConfigurationBO createDeviceConfiguration(EntityDevice device, int domainIndex) {
        EntityConfigurationBO deviceConfig = new EntityConfigurationBO();
        deviceConfig.setCloudId(device.getCloudId());
        String version = device.getDeviceAdditionalInfo().getSpecVersion();
        deviceConfig.setSpecVersion(version);
        deviceConfig.setDomainIndex(String.valueOf(domainIndex));
        List<LinkBO> configurations = deviceConfig.getConfigurations();

        LinkBO connectivityConfig = new LinkBO();
        connectivityConfig.setRel(ServiceInstanceType.CONNECTIVITY_CONFIG.getValue());
        ServiceInstance serviceInstanceForConnnectivityUrl = deviceServiceInstanceRepository
                .findByServiceTypeAndSpecVersion(ServiceInstanceType.CONNECTIVITY_CONFIG.getValue(), version);

        if (serviceInstanceForConnnectivityUrl == null || StringUtils.isEmpty(serviceInstanceForConnnectivityUrl.getUrl())) {
            throw new EmptyConfigUrlException("CONNECTIVITY_CONFIG service url not exist");
        }

        String cmsURL = serviceInstanceForConnnectivityUrl.getUrl();
        connectivityConfig.setHref(String.format(cmsURL, device.getCloudId()));


        LinkBO printCaps = new LinkBO();
        printCaps.setRel(ServiceInstanceType.PRINTER_CAPS.getValue());
        ServiceInstance serviceInstanceForPrintCapsUrl = deviceServiceInstanceRepository
                .findByServiceTypeAndSpecVersion(ServiceInstanceType.PRINTER_CAPS.getValue(), version);

        if (serviceInstanceForPrintCapsUrl == null || StringUtils.isEmpty(serviceInstanceForPrintCapsUrl.getUrl())) {
            throw new EmptyConfigUrlException("PRINTER_CAPS service url not exist");
        }

        String printCapsURL = serviceInstanceForPrintCapsUrl.getUrl();
        printCaps.setHref(String.format(printCapsURL, device.getCloudId()));

        if (registrationProcessorConfig.isRegResponseHasCredentialRefreshURLEnabled()) {
            LinkBO credentialRefresh = new LinkBO();
            ServiceInstance serviceInstanceForCredentialRefreshUrl = deviceServiceInstanceRepository.findByServiceTypeAndSpecVersion(ServiceInstanceType.CREDENTIAL_REFRESH.getValue(), version);
            if (serviceInstanceForCredentialRefreshUrl == null || StringUtils.isEmpty(serviceInstanceForCredentialRefreshUrl.getUrl())) {
                throw new EmptyConfigUrlException("CREDENTIAL_REFRESH service url not exist");
            }

            String credentialRefreshURL = serviceInstanceForCredentialRefreshUrl.getUrl();
            credentialRefresh.setHref(String.format(credentialRefreshURL, device.getCloudId()));
            credentialRefresh.setRel(ServiceInstanceType.CREDENTIAL_REFRESH.getValue());
            configurations.add(credentialRefresh);
        }

        configurations.add(connectivityConfig);
        configurations.add(printCaps);

        return deviceConfig;
    }

    private void reRegisterDevice(EntityDevice deviceDB, EntityDevice device) {
        entityDeviceRepository.delete(deviceDB);
        createDevice(device);
        LOG.debug("Device is deleted and new device is being created");
    }

    private String generateCloudId() {
        byte entityTypeValue = (byte) EntityType.devices.getValue();
        return cloudIdGenerator.newCloudID(getPodCode(), entityTypeValue);
    }

    private void createDevice(EntityDevice device,
                              HashedEntityIdentifier hashedSerial) {
        String deviceId = generateCloudId();
        device.setCloudId(deviceId);
        device.getDeviceAdditionalInfo().setCloudId(deviceId);
        LOG.debug("New registration with serialNumber := "
                + device.getEntityId());
        try {
            entityDeviceRepository.persistHashedIdentifier(hashedSerial);
        } catch (DataIntegrityViolationException ex) {

            LOG.debug("Duplicate serial number insertion ignored for serial number:= "
                    + device.getEntityId());

        }
        entityDeviceRepository.persistEntityDevices(device);
        LOG.debug("Device registered with cloud-id := "
                + device.getCloudId());
    }

    private void createDevice(EntityDevice device) {
        String cloudId = generateCloudId();
        device.setCloudId(cloudId);
        device.getDeviceAdditionalInfo().setCloudId(cloudId);
        entityDeviceRepository.save(device);
        LOG.debug("Device registered with cloud-id := "
                + device.getCloudId());
    }

    public static String generateSHA256AndBase64(byte[] serialNumberInBytes) {
        byte[] hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance(SHA256_ALGORITHM_NAME);
            hash = md.digest(serialNumberInBytes);
        } catch (Exception e) {
            LOG.error("Exception while hashing serial number; failureReason={}", e);
        }
        return new String(Base64.encodeBase64(hash));
    }

    public HashedEntityIdentifier generateHashedSerialNumber(String serialNumber) {
        byte[] serialNumberInBytes = serialNumber.getBytes();

        HashedEntityIdentifier hashed = new HashedEntityIdentifier();
        hashed.setEntityIdentifier(serialNumber);
        hashed.setHashedEntityIdentifier(generateSHA256AndBase64(serialNumberInBytes));
        return hashed;
    }

    private void updateOnReRegister(EntityDevice deviceDB, EntityDevice device) {
        device.setCloudId(deviceDB.getCloudId());
        device.getDeviceAdditionalInfo().setCloudId(deviceDB.getCloudId());
        device.setId(deviceDB.getId());
        entityDeviceRepository.updateDeviceAdditionalInfo(device
                .getDeviceAdditionalInfo().getCloudId(), device
                .getDeviceAdditionalInfo().getEntityRevision(), device
                .getDeviceAdditionalInfo().getEntityVersionDate(), device
                .getDeviceAdditionalInfo().getCountryAndRegionName(), device
                .getDeviceAdditionalInfo().getLanguage(), device
                .getDeviceAdditionalInfo().getSpecVersion(), device
                .getDeviceAdditionalInfo().getOriginator(), device
                .getDeviceAdditionalInfo().getEntityAdditionalIds(), device
                .getDeviceAdditionalInfo().getEntityInfo(), device.getDeviceAdditionalInfo().getEntityMCID());
    }

    @Override
    public RegisteredEntityBO getRegisteredEntity(String cloudId) throws EntityRegistrationNonRetriableException {
        EntityDevice device = entityDeviceRepository.findByCloudId(cloudId);
        if (device == null) {
            LOG.debug("Device registration status check :: Cloud-Id=" + cloudId + "; Status=NotRegistered");
            throw new EntityNotRegisteredException("Device not registered with cloudId=" + cloudId);
        }
        LOG.debug("Device registration status check :: Cloud-Id=" + cloudId + "; serialNumber=" + device.getEntityId() + "; Status=Registered");

        return createRegisteredEntityBO(device);
    }

    @Override
    public void validateEntityIdentificationBO(EntityIdentificationBO entityIdentificationBO) throws EntityRegistrationNonRetriableException {
        if (entityIdentificationBO.getEntityUUID() == null)
            throw new InvalidRegistrationDataException("Device payload MUST have printer-uuid");
    }

    @Override
    public EntityIdentificationBO getEntityIdentificationBO(String cloudId) throws EntityRegistrationNonRetriableException {
        EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();
        EntityDevice device = entityDeviceRepository.getDeviceWithDeviceAdditionalInfo(cloudId);
        if (device == null)
            throw new EntityNotRegisteredException("Not a valid registered entity of type devices");

        try {
            BeanUtils.copyProperties(entityIdentificationBO, device);
            BeanUtils.copyProperties(entityIdentificationBO, device.getDeviceAdditionalInfo());

            if (StringUtils.isEmpty(entityIdentificationBO.getEntityUUID())) {
                LOG.error("Printer-UUID MUST not be empty for Entity-Type [Devices] for cloudId={}", cloudId);
            }
        } catch (Exception e) {
            throw new ModellingException("Exception while copying DB content to BO for cloudId=" + cloudId, e);
        }
        HashedEntityIdentifier hashedEntityIdentifier = hashedEntityIdentifierRepository.findByHashedEntityIdentifier(entityIdentificationBO.getEntityId());
        entityIdentificationBO.setEntityId(hashedEntityIdentifier.getEntityIdentifier());

        return entityIdentificationBO;
    }

    public static EntityDevice createEntityDevice(EntityIdentificationBO entityIdentification) {
        EntityDevice device = new EntityDevice();
        DeviceAdditionalInfo deviceInfo = new DeviceAdditionalInfo();

        try {
            BeanUtils.copyProperties(device, entityIdentification);
            BeanUtils.copyProperties(deviceInfo, entityIdentification);
            device.setDeviceAdditionalInfo(deviceInfo);
            deviceInfo.setDevice(device);
        } catch (Exception e) {
            throw new ModellingException("Exception while copying business object bean to model", e);
        }
        return device;
    }

    private RegisteredEntityBO createRegisteredEntityBO(EntityDevice device) {
        RegisteredEntityBO entityRegisteredBO = new RegisteredEntityBO();
        try {
            BeanUtils.copyProperties(entityRegisteredBO, device);
            entityRegisteredBO.setEntityType(EntityType.devices.name());
        } catch (Exception e) {
            throw new ModellingException("Exception while copying business object bean to model", e);
        }
        return entityRegisteredBO;
    }

}
