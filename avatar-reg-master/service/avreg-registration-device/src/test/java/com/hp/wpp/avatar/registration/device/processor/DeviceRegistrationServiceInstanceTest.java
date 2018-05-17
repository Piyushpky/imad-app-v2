package com.hp.wpp.avatar.registration.device.processor;

import com.hp.wpp.avatar.framework.common.config.RegistrationProcessorConfig;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.registration.device.entities.DeviceAdditionalInfo;
import com.hp.wpp.avatar.registration.device.entities.EntityDevice;
import com.hp.wpp.avatar.registration.device.entities.ServiceInstance;
import com.hp.wpp.avatar.registration.device.entities.domain.RegistrationDomain;
import com.hp.wpp.avatar.registration.device.repository.DeviceServiceInstanceRepository;
import com.hp.wpp.avatar.registration.device.repository.EntityDeviceRepository;
import com.hp.wpp.avatar.registration.device.repository.HashedEntityIdentifierRepository;
import com.hp.wpp.avatar.registration.device.repository.RegistrationDomainRepository;
import com.hp.wpp.cidgenerator.impl.CloudIdGeneratorImpl;
import com.hp.wpp.http.WppHttpClient;
import com.hp.wpp.ssn.SignedSerialNumberValidator;
import com.hp.wpp.stream.messages.client.EventProducer;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;

public class DeviceRegistrationServiceInstanceTest {

    private static final String TEST_DOMAIN = "test_domain";
    private static final String ENTITY_MODEL = "entity-model";
    private static final String ENTITY_REVISION = "entity-revision";
    public static final String ENTITY_NAME = "entity-name";
    public static final String ENTITY_INFO = "entity-info";
    public static final String ENTITY_VERSION_DATE = "01-01-2015";
    public static final String LANGUAGE = "en";
    public static final String ORIGINATOR = "ews";
    public static final String SPEC_VERSION = "1.0";
    public static final String COUNTRY = "india";
    public static final String ENTITY_ADDITIONAL_IDS = "entity-additional-ids";
    private String entityId;

    @Mock
    private EntityDeviceRepository entityDeviceRepository;
    @Mock
    private HashedEntityIdentifierRepository hashedEntityIdentifierRepository;
    @Mock
    private RegistrationDomainRepository registrationDomainRepository;

    @Mock private DeviceServiceInstanceRepository serviceInstanceRepository;

    @Mock private SignedSerialNumberValidator ssnValidator;

    @Mock private RegistrationProcessorConfig registrationProcessorConfig;

    @Mock private WppHttpClient wppHttpClient;

    @Mock private ThreadPoolExecutor threadPoolExecutor;

    @Mock private EventProducer eventProducer;

    private DeviceRegistrationProcessor deviceRegistration;

    private EntityIdentificationBO entityIdentification;

    private CloudIdGeneratorImpl cloudIdGenerator;

    public enum ErrorCases {
        CONN_CONFIG_NULL, CONN_CONFIG_EMPTYURL, PRINT_CAPS_NULL ,  PRINT_CAPS_EMPTYURL, CRED_REFRESH_NULL, CRED_REFRESH_EMPTYURL
    }

    public void initDeviceRegistration(){
        MockitoAnnotations.initMocks(this);
        deviceRegistration = new DeviceRegistrationProcessor(wppHttpClient,threadPoolExecutor);
        deviceRegistration.setEntityDeviceRepository(entityDeviceRepository);
        deviceRegistration.setDeviceServiceInstanceRepository(serviceInstanceRepository);
        deviceRegistration.setHashedEntityIdentifierRepository(hashedEntityIdentifierRepository);
        deviceRegistration.setRegistrationDomainRepository(registrationDomainRepository);
        deviceRegistration.setSsnValidator(ssnValidator);
        cloudIdGenerator = new CloudIdGeneratorImpl();
        deviceRegistration.setCloudIdGenerator(cloudIdGenerator);
        deviceRegistration.setPodCode((short) 0);
        deviceRegistration.setDeRegistrationStreamName("DeleteStream");
        deviceRegistration.setRegistrationProcessorConfig(registrationProcessorConfig);
        deviceRegistration.setEventProducer(eventProducer);
        entityIdentification=createEntityIdentificationBO(1);
        Mockito.when(registrationProcessorConfig.isRegResponseHasCredentialRefreshURLEnabled()).thenReturn(true);
        RegistrationDomain domain = new RegistrationDomain();
        domain.setDomainId(1);
        domain.setDomainIndex(0);
        domain.setEntityDomain("test_domain");
        Mockito.when(registrationDomainRepository.findByRegistrationDomain(Mockito.anyString())).thenReturn(domain);
        Mockito.doNothing().when(threadPoolExecutor).execute(any(DeviceClaimProcessor.class));
    }

    private EntityDevice createEntityDevice(int resetCounter) {
        EntityDevice device = new EntityDevice();
        String cloudId = UUID.randomUUID().toString().replaceAll("-", "");
        device.setCloudId(cloudId);
        entityId = UUID.randomUUID().toString().replaceAll("-", "");
        device.setEntityId(entityId);
        device.setEntityDomain(TEST_DOMAIN);
        device.setEntityModel(ENTITY_MODEL);
        device.setEntityName(ENTITY_NAME);
        device.setResetCounter(resetCounter);
        device.setEntityUUID(entityId);

        DeviceAdditionalInfo deviceAdditionalInfo = new DeviceAdditionalInfo();
        deviceAdditionalInfo.setCloudId(cloudId);
        deviceAdditionalInfo.setCountryAndRegionName(COUNTRY);
        deviceAdditionalInfo.setEntityAdditionalIds(ENTITY_ADDITIONAL_IDS);
        deviceAdditionalInfo.setEntityInfo(ENTITY_INFO);
        deviceAdditionalInfo.setEntityRevision(ENTITY_REVISION);
        deviceAdditionalInfo.setEntityVersionDate(ENTITY_VERSION_DATE);
        deviceAdditionalInfo.setLanguage(LANGUAGE);
        deviceAdditionalInfo.setOriginator(ORIGINATOR);
        deviceAdditionalInfo.setSpecVersion(SPEC_VERSION);

        deviceAdditionalInfo.setDevice(device);
        assertNull(device.getDeviceAdditionalInfo());
        device.setDeviceAdditionalInfo(deviceAdditionalInfo);

        deviceAdditionalInfo = device.getDeviceAdditionalInfo();
        device.setDeviceAdditionalInfo(deviceAdditionalInfo);
        return device;
    }

    private EntityIdentificationBO createEntityIdentificationBO(int resetCounter) {
        EntityDevice device = createEntityDevice(resetCounter);

        EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();
        BeanUtils.copyProperties(device, entityIdentificationBO);
        BeanUtils.copyProperties(device.getDeviceAdditionalInfo(),entityIdentificationBO);

        return entityIdentificationBO;
    }

    @DataProvider(name = "serviceInstanceInvalidRecords")
    public Object[][] serviceInstanceInvalidRecords(){
        return new Object[][]{
                {ErrorCases.CONN_CONFIG_NULL},{ErrorCases.CONN_CONFIG_EMPTYURL},{ErrorCases.PRINT_CAPS_NULL},{ErrorCases.PRINT_CAPS_EMPTYURL}, {ErrorCases.CRED_REFRESH_NULL}, {ErrorCases.CRED_REFRESH_EMPTYURL}
        };
    }

    private int mockCredRefreshToEmptyURL() {
        initDeviceRegistration();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setUrl("http://16.183.206.123:8008/virtualprinter/v1/printers/%s/printerconfig");
        ServiceInstance emptyURL = new ServiceInstance();
        Mockito.when(serviceInstanceRepository.findByServiceTypeAndSpecVersion(Mockito.anyString(), Mockito.anyString())).thenReturn(serviceInstance).thenReturn(serviceInstance).thenReturn(emptyURL);
        return 0;
    }

    private int mockCredRefreshToNull() {
        initDeviceRegistration();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setUrl("http://16.183.206.123:8008/virtualprinter/v1/printers/%s/printerconfig");
        Mockito.when(serviceInstanceRepository.findByServiceTypeAndSpecVersion(Mockito.anyString(), Mockito.anyString())).thenReturn(serviceInstance).thenReturn(serviceInstance).thenReturn(null);
        return 0;
    }

    private int mockPrintCapsToEmptyURL() {
        initDeviceRegistration();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setUrl("http://16.183.206.123:8008/virtualprinter/v1/printers/%s/printerconfig");
        ServiceInstance emptyURL = new ServiceInstance();
        Mockito.when(serviceInstanceRepository.findByServiceTypeAndSpecVersion(Mockito.anyString(), Mockito.anyString())).thenReturn(serviceInstance).thenReturn(emptyURL);
        return 0;
    }

    private int mockPrintCapsToNull() {
        initDeviceRegistration();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setUrl("http://16.183.206.123:8008/virtualprinter/v1/printers/%s/connConfig");
        Mockito.when(serviceInstanceRepository.findByServiceTypeAndSpecVersion(Mockito.anyString(), Mockito.anyString())).thenReturn(serviceInstance).thenReturn(null);
        return 0;
    }

    private int mockConnectivityConfigToEmptyURL() {
        initDeviceRegistration();
        ServiceInstance serviceInstance = new ServiceInstance();
        Mockito.when(serviceInstanceRepository.findByServiceTypeAndSpecVersion(Mockito.anyString(), Mockito.anyString())).thenReturn(serviceInstance);
        return 0;
    }

    private int mockConnectivityConfigToNull() {
        initDeviceRegistration();
        Mockito.when(serviceInstanceRepository.findByServiceTypeAndSpecVersion(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        return 0;
    }


    @Test(dataProvider = "serviceInstanceInvalidRecords", expectedExceptions = {EntityRegistrationNonRetriableException.class})
    public void testDeviceRegistrationWhenServiceInstanceHasNoRecords(ErrorCases errorCase) throws Exception{
        mockRequiredServiceInstanceRecord(errorCase);
        when(entityDeviceRepository.findByEntityIdAndEntityModel(any(String.class), any(String.class))).thenReturn(null);
        deviceRegistration.registerEntity(entityIdentification);
    }

    private void mockRequiredServiceInstanceRecord(ErrorCases errorCase) {
        switch (errorCase){
            case CONN_CONFIG_NULL:
                mockConnectivityConfigToNull();
                break;
            case CONN_CONFIG_EMPTYURL:
                mockConnectivityConfigToEmptyURL();
                break;
            case PRINT_CAPS_NULL:
                mockPrintCapsToNull();
                break;
            case PRINT_CAPS_EMPTYURL:
                mockPrintCapsToEmptyURL();
                break;
            case CRED_REFRESH_NULL:
                mockCredRefreshToNull();
                break;
            case CRED_REFRESH_EMPTYURL:
                mockCredRefreshToEmptyURL();
                break;
        }
    }
}
