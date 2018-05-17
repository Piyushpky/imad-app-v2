package com.hp.wpp.avatar.registration.service.processor;

import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.avatar.framework.common.config.RegistrationProcessorConfig;
import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.enums.ServiceInstanceType;
import com.hp.wpp.avatar.framework.eventnotification.schema.DeRegisterEventNotificationMessage;
import com.hp.wpp.avatar.framework.eventnotification.schema.EventDetails;
import com.hp.wpp.avatar.framework.eventnotification.schema.EventOriginator;
import com.hp.wpp.avatar.framework.exceptions.EmptyConfigUrlException;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.ModellingException;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.EntityConfigurationBO;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.framework.processor.data.LinkBO;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.registration.service.entities.EntityService;
import com.hp.wpp.avatar.registration.service.entities.HashedEntityIdentifier;
import com.hp.wpp.avatar.registration.service.entities.ServiceAdditionalInfo;
import com.hp.wpp.avatar.registration.service.entities.ServiceInstance;
import com.hp.wpp.avatar.registration.service.repository.EntityServiceRepository;
import com.hp.wpp.avatar.registration.service.repository.ServiceInstanceRepository;
import com.hp.wpp.cidgenerator.CloudIdGenerator;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.stream.messages.client.EventProducer;

public class ServiceRegistrationProcessor implements EntityRegistrationProcessor{

	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(ServiceRegistrationProcessor.class);

	private static final String SHA256_ALGORITHM_NAME = "SHA-256";

	private short podCode;

	@Autowired
	private EntityServiceRepository entityServiceRepository;

	@Autowired
	private ServiceInstanceRepository serviceInstanceRepository;

	@Autowired
	private CloudIdGenerator cloudIdGenerator;

	@Autowired
	private RegistrationProcessorConfig registrationProcessorConfig;

	private String deRegistrationStreamName;

	private static final String SERVICE_NAME="avatar-reg";

	@Autowired
	private EventProducer eventProducer;


	public void setEventProducer(EventProducer eventProducer) {
		this.eventProducer = eventProducer;
	}

	public void setRegistrationProcessorConfig(RegistrationProcessorConfig registrationProcessorConfig) {
		this.registrationProcessorConfig = registrationProcessorConfig;
	}

	public String getDeRegistrationStreamName() {
		return deRegistrationStreamName;
	}

	public void setDeRegistrationStreamName(String deRegistrationStreamName) {
		this.deRegistrationStreamName = deRegistrationStreamName;
	}

	public short getPodCode() {
		return podCode;
	}

	public void setPodCode(short podCode) {
		this.podCode = podCode;
	}

	public void setEntityServiceRepository(EntityServiceRepository entityServiceRepository) {
		this.entityServiceRepository = entityServiceRepository;
	}

	public void setServiceInstanceRepository(ServiceInstanceRepository serviceInstanceRepository) {
		this.serviceInstanceRepository = serviceInstanceRepository;
	}

	public void setCloudIdGenerator(CloudIdGenerator cloudIdGenerator) {
		this.cloudIdGenerator = cloudIdGenerator;
	}

	@Override
	public EntityConfigurationBO registerEntity(EntityIdentificationBO entityIdentification) throws EntityRegistrationNonRetriableException {
		EntityService entityService = createEntityService(entityIdentification);
		HashedEntityIdentifier hashedEntityIdentifier = generateHashedSerialNumber(entityService.getEntityId());

		String hashedEntityId = hashedEntityIdentifier.getHashedEntityIdentifier();
		entityService.setEntityId(hashedEntityId);

		EntityService entityServiceDB = entityServiceRepository.findByEntityIdAndEntityModel(hashedEntityId, entityService.getEntityModel());
		if(entityServiceDB == null){
			LOG.debug("New service registration with entityId := "+ entityService.getEntityId());
			persistEnityServiceWithHashedIdentifier(entityService, hashedEntityIdentifier);
		}else{
			entityService.setCloudId(entityServiceDB.getCloudId());
			LOG.debug("Re-registration flow initiated for Service with entity-Id: {}, cloud-Id: {}", entityService.getEntityId(), entityService.getCloudId());

			if (entityService.getResetCounter() == entityServiceDB.getResetCounter()) {
				LOG.debug("Updating Service additional info entity with for cloudId={}",entityService.getCloudId());
				updateOnReRegister(entityService);
			}else {
				String  oldCloudId = entityService.getCloudId();
				LOG.debug("Considered as a new registration request for entity-Id: {}",entityService.getEntityId());
				reRegisterEntityService(entityServiceDB, entityService);
				LOG.debug("Notify de-registration event for old_cloud_id={}, updated with new cloud_id={}", oldCloudId,entityService.getCloudId());
				notifyDeRegistrationEvent(oldCloudId);
			}
		}
		return createEntityConfiguration(entityService);
	}

	private void notifyDeRegistrationEvent(String cloudId) {

		try {

			DeRegisterEventNotificationMessage deRegisterEventNotificationMessage = new DeRegisterEventNotificationMessage();
			deRegisterEventNotificationMessage.setVersion("1.0.0");
			deRegisterEventNotificationMessage.setSubscriptionId("sub"+cloudId);
			deRegisterEventNotificationMessage.setEventOriginator(buildEventOriginator());

			EventDetails eventDetails = new EventDetails();
			eventDetails.setEventName("De-Registration");
			eventDetails.setEntityId(cloudId);
			deRegisterEventNotificationMessage.setEventDetails(eventDetails);

			eventProducer.sendNotification(deRegistrationStreamName,deRegisterEventNotificationMessage.toJsonAsBytes());
			LOG.debug("Notified de-registration event for cloud_id={}", cloudId);
		} catch (Exception ex) {

			LOG.error("api=notifyDeRegistrationEvent; executionType=SyncFlow; cloudId={}; executionState=COMPLETED; status=FAILURE; failureReason=\"{}\";", cloudId, ex.getMessage());
		}
	}

	private EventOriginator buildEventOriginator() {
		EventOriginator eventOriginator = new EventOriginator();
		eventOriginator.setSource(SERVICE_NAME);
		eventOriginator.setTimeOfCreation(new Date().toString());
		return eventOriginator;
	}

	@Override
	public RegisteredEntityBO getRegisteredEntity(String cloudId) throws  EntityRegistrationNonRetriableException {
		EntityService service = entityServiceRepository.findByCloudId(cloudId);
		if (service == null) {
			LOG.debug("Service registration status check :: cloud-Id= {}; Status = NotRegistered", cloudId);
			throw new EntityNotRegisteredException("No Service registration found with cloudId="+cloudId);
		}
		LOG.debug("Service registration status check :: cloud-Id= {}; Entity-Id= {}; Status = Registered",cloudId,service.getEntityId());
		return createEntityServiceBO(service);
	}

	@Override
	public EntityIdentificationBO getEntityIdentificationBO(String cloudId) throws EntityRegistrationNonRetriableException {
		return null;
	}

	@Override
	public void validateEntityIdentificationBO(EntityIdentificationBO entityIdentificationBO)  throws EntityRegistrationNonRetriableException{
		// As of now validation is not required for Service specific attributes
	}

	public static RegisteredEntityBO createEntityServiceBO(EntityService service) {
		RegisteredEntityBO entityServiceBO = new RegisteredEntityBO();
		try {
			BeanUtils.copyProperties(entityServiceBO, service);
			entityServiceBO.setEntityType(EntityType.services.name());
		} catch (Exception e) {
			throw new ModellingException("Exception while copying business object bean to model",e);
		}
		return entityServiceBO;
	}

	public HashedEntityIdentifier generateHashedSerialNumber(String entityId) {
		byte[] entityIdInBytes = entityId.getBytes();

		HashedEntityIdentifier hashed = new HashedEntityIdentifier(entityId,generateSHA256AndBase64(entityIdInBytes));
		return hashed;
	}

	public static String generateSHA256AndBase64(byte[] entityIdInBytes) {
		byte[] hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance(SHA256_ALGORITHM_NAME);
			hash = md.digest(entityIdInBytes);
		} catch (Exception e) {
			LOG.error("Exception while hashing serial number; failureReason={}",e);
		}
		return new String(Base64.encodeBase64(hash));
	}

	private void updateOnReRegister(EntityService entityService) {
		entityServiceRepository.updateServieAdditionalInfo(entityService.getCloudId(),
				entityService.getServiceAdditionalInfo().getEntityRevision(),
				entityService.getServiceAdditionalInfo().getEntityVersionDate(),
				entityService.getServiceAdditionalInfo().getCountryAndRegionName(),
				entityService.getServiceAdditionalInfo().getLanguage(),
				entityService.getServiceAdditionalInfo().getSpecVersion(),
				entityService.getServiceAdditionalInfo().getOriginator(),
				entityService.getServiceAdditionalInfo().getEntityAdditionalIds(),
				entityService.getServiceAdditionalInfo().getEntityInfo());
	}

	private EntityConfigurationBO createEntityConfiguration(EntityService entityService) {
		EntityConfigurationBO config = new EntityConfigurationBO();
		String specVersion = entityService.getServiceAdditionalInfo().getSpecVersion();
		config.setSpecVersion(specVersion);
		config.setCloudId(entityService.getCloudId());

		List<LinkBO> configurations = config.getConfigurations();

		//Connectivity-configuration
		LinkBO connectivityConfig = new LinkBO();
		connectivityConfig.setRel(ServiceInstanceType.CONNECTIVITY_CONFIG.getValue());
		ServiceInstance serviceInstance = serviceInstanceRepository.findByServiceTypeAndSpecVersion(ServiceInstanceType.CONNECTIVITY_CONFIG.getValue(), specVersion);
		if(serviceInstance == null){
			throw new EmptyConfigUrlException("CONNECTIVITY_CONFIG service url should not be null");
		}
		connectivityConfig.setHref(String.format(serviceInstance.getUrl(), entityService.getCloudId()));

		//service configuration
		LinkBO serviceConfig = new LinkBO();
		serviceConfig.setRel(ServiceInstanceType.SERVICE_CONFIG.getValue());
		serviceInstance = serviceInstanceRepository.findByServiceTypeAndSpecVersion(ServiceInstanceType.SERVICE_CONFIG.getValue(), specVersion);
		if(serviceInstance == null){
			throw new EmptyConfigUrlException("SERVICE_CONFIG service url should not be null");
		}
		serviceConfig.setHref(String.format(serviceInstance.getUrl(), entityService.getCloudId()));

		//credential_refresh
		if (registrationProcessorConfig.isRegResponseHasCredentialRefreshURLEnabled()) {
			LinkBO credentialRefresh = new LinkBO();
			credentialRefresh.setRel(ServiceInstanceType.CREDENTIAL_REFRESH.getValue());
			serviceInstance = serviceInstanceRepository.findByServiceTypeAndSpecVersion(ServiceInstanceType.CREDENTIAL_REFRESH.getValue(),specVersion);
			if (serviceInstance == null) {
				throw new EmptyConfigUrlException("CREDENTIAL_REFRESH service url should not be null");
			}
			credentialRefresh.setHref(String.format(serviceInstance.getUrl(), entityService.getCloudId()));
			configurations.add(credentialRefresh);
		}

		configurations.add(connectivityConfig);
		configurations.add(serviceConfig);

		return config ;
	}

	private void reRegisterEntityService(EntityService entityServiceDB, EntityService entityService) {
		LOG.debug("Service is deleted and new registration entries are being created");
		entityServiceRepository.delete(entityServiceDB.getEntityServiceId());
		registerNewService(entityService);
	}

	private void registerNewService(EntityService entityService) {
		generateAndUpdateEntityServiceWithCloudID(entityService);
		entityServiceRepository.save(entityService);
		LOG.debug("Service registered with cloudId := " + entityService.getCloudId());
	}

	private void generateAndUpdateEntityServiceWithCloudID(EntityService entityService) {
		byte entityTypeValue = (byte)EntityType.services.getValue();
		String cloudId = cloudIdGenerator.newCloudID(podCode, entityTypeValue );
		entityService.setCloudId(cloudId);
		entityService.getServiceAdditionalInfo().setCloudId(cloudId);
	}

	private void persistEnityServiceWithHashedIdentifier(EntityService service, HashedEntityIdentifier hashedEntityIdentifier) {
		generateAndUpdateEntityServiceWithCloudID(service);

		entityServiceRepository.persistEntityServices(service, hashedEntityIdentifier);
		LOG.debug("Service registered with cloudId := " + service.getCloudId());
	}

	private EntityService createEntityService(EntityIdentificationBO entityIdentification) {
		EntityService service  = new EntityService();
		ServiceAdditionalInfo serviceAdditionalInfo = new ServiceAdditionalInfo();
		try{
			BeanUtils.copyProperties(service, entityIdentification);
			BeanUtils.copyProperties(serviceAdditionalInfo, entityIdentification);
			service.setServiceAdditionalInfo(serviceAdditionalInfo);
			serviceAdditionalInfo.setEntityService(service);
		}catch(Exception e){
			throw new ModellingException("Exception while copying business object bean to model",e);
		}
		return service;
	}
}
