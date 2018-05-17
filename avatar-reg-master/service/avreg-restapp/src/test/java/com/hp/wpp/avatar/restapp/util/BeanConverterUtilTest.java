package com.hp.wpp.avatar.restapp.util;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.avatar.restmodel.enums.AdditionalIdType;
import com.hp.wpp.avatar.restmodel.enums.Country;
import com.hp.wpp.avatar.restmodel.enums.Language;
import com.hp.wpp.avatar.restmodel.enums.Originator;
import com.hp.wpp.avatar.restmodel.json.schema.EntityIdentification;
import com.hp.wpp.avatar.restmodel.json.schema.EntityInformation;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;


public class BeanConverterUtilTest {

    public static final String ENTITY_UUID = "entity_uuid";
    public static final String ORIGINATOR = "ews";
    public static final String ENTITY_INFO = "entity_info";
    public static final Country UNITED_STATES = Country.unitedStates;
    public static final Language LANGUAGE = Language.EnglishUS;
    private static final String SPEC_VERSION = "1.0";
    private static final String DATE = "2015-01-01";
    private static final String REVISION = "revision";
    private static final String ENTITY_NAME = "entity-name";
    private static final String ENTITY_MODEL = "entity-model";
    private static final String ENTITY_ID = "entity-id";
    private static final String PRINTER_UUID = "printer-uuid";
    private static final String PRINTER_MCID = "printer-mcid";
    private static final String SSN_VALUE = "ssn-value";
    private static final String ENTITY_DOMAIN = "entity-domain";

    private EntityTypeProcessorMap entityTypeProcessorMap;
    @Mock
    private EntityRegistrationProcessor entityRegistrationProcessor;

    @BeforeMethod
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        Map<EntityType, EntityRegistrationProcessor> entityTypeToProcessorMap = new HashMap<EntityType, EntityRegistrationProcessor>();
        entityTypeToProcessorMap.put(EntityType.devices, entityRegistrationProcessor);
        entityTypeToProcessorMap.put(EntityType.services, entityRegistrationProcessor);

        entityTypeProcessorMap = new EntityTypeProcessorMap();
        entityTypeProcessorMap.setEntityTypeToProcessorMap(entityTypeToProcessorMap);

        Mockito.doNothing().when(entityRegistrationProcessor).validateEntityIdentificationBO((EntityIdentificationBO) Mockito.anyObject());
    }

    @Test
    public void testCreateServiceEntityInformationFromBO() throws Exception{
        EntityInformation entityInformation = BeanConverterUtil.createEntityInformationFromBO(getEntityIdentificationBO(EntityType.services));
        Assert.assertNotNull(entityInformation);
        Assert.assertEquals(entityInformation.getVersion(), SPEC_VERSION);
        Assert.assertEquals(entityInformation.getEntityId(), ENTITY_ID);
        Assert.assertEquals(entityInformation.getEntityModel(), ENTITY_MODEL);
        Assert.assertEquals(entityInformation.getEntityName(), ENTITY_NAME);
        Assert.assertEquals(entityInformation.getCountryAndRegionName(), UNITED_STATES);
        Assert.assertEquals(entityInformation.getEntityVersion().getRevision(), REVISION);
        Assert.assertEquals(entityInformation.getEntityVersion().getDate(), DATE);
        Assert.assertEquals(entityInformation.getLanguage(), LANGUAGE);

        Assert.assertNull(entityInformation.getEntityAdditionalIds());
    }

    @DataProvider(name="device_entity_information")
    public Object[][] getEntityInformation(){
        return new Object[][]{
                {getEntityIdentificationBO(EntityType.devices),LANGUAGE },
                {getEntityIdentificationBO(EntityType.devices, Language.SimplifiedChinese.toString()),Language.SimplifiedChinese}
        };
    }

    @Test(dataProvider = "device_entity_information")
    public void testCreateDeviceEntityInformationFromBO(EntityIdentificationBO entityIdentificationBO, Language lang) throws Exception{
        EntityInformation entityInformation = BeanConverterUtil.createEntityInformationFromBO(entityIdentificationBO);

        Assert.assertNotNull(entityInformation);
        Assert.assertEquals(entityInformation.getVersion(), SPEC_VERSION);
        Assert.assertEquals(entityInformation.getEntityId(), ENTITY_ID);
        Assert.assertEquals(entityInformation.getEntityModel(), ENTITY_MODEL);
        Assert.assertEquals(entityInformation.getEntityName(), ENTITY_NAME);
        Assert.assertEquals(entityInformation.getCountryAndRegionName(), UNITED_STATES);
        Assert.assertEquals(entityInformation.getEntityVersion().getRevision(), REVISION);
        Assert.assertEquals(entityInformation.getEntityVersion().getDate(), DATE);
        Assert.assertEquals(entityInformation.getLanguage(), lang);

        Assert.assertNotNull(entityInformation.getEntityAdditionalIds());
        EntityInformation.EntityAdditionalId additionalId= entityInformation.getEntityAdditionalIds().get(0);
        Assert.assertNotNull(additionalId);
        Assert.assertEquals(additionalId.getIdValue(), ENTITY_UUID);
    }

    private EntityIdentificationBO getEntityIdentificationBO(EntityType entityType) {
        return getEntityIdentificationBO(entityType,LANGUAGE.toString());
    }

    private EntityIdentificationBO getEntityIdentificationBO(EntityType entityType,String lang) {
        EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();
        entityIdentificationBO.setSpecVersion(SPEC_VERSION);
        entityIdentificationBO.setEntityId(ENTITY_ID);
        entityIdentificationBO.setEntityDomain(ENTITY_DOMAIN);
        entityIdentificationBO.setEntityModel(ENTITY_MODEL);
        entityIdentificationBO.setEntityName(ENTITY_NAME);
        entityIdentificationBO.setResetCounter(2);

        if(entityType.equals(EntityType.devices))
            entityIdentificationBO.setEntityUUID(ENTITY_UUID);

        entityIdentificationBO.setCountryAndRegionName(UNITED_STATES.name());
        entityIdentificationBO.setEntityAdditionalIds("entity_additional_ids");
        entityIdentificationBO.setEntityInfo(ENTITY_INFO);
        entityIdentificationBO.setEntityRevision(REVISION);
        entityIdentificationBO.setEntityVersionDate(DATE);
        entityIdentificationBO.setLanguage(lang);
        entityIdentificationBO.setOriginator(ORIGINATOR);

        return entityIdentificationBO;
    }

    @Test
    public void testCreateEntityIdentificationBO()throws Exception{
        EntityIdentification entityIdentification = new EntityIdentification();
        entityIdentification.setCountryAndRegionName(Country.unitedStates);
        entityIdentification.setLanguage(Language.SimplifiedChinese);

        List<EntityIdentification.EntityAdditionalId> entityAdditionalIds = new ArrayList<EntityIdentification.EntityAdditionalId>();
        EntityIdentification.EntityAdditionalId ssn = new EntityIdentification.EntityAdditionalId();
        ssn.setIdType(AdditionalIdType.ssn.name());
        ssn.setIdValue(SSN_VALUE);
        entityAdditionalIds.add(ssn);

        EntityIdentification.EntityAdditionalId printerUUID = new EntityIdentification.EntityAdditionalId();
        printerUUID.setIdType(AdditionalIdType.printer_uuid.name());
        printerUUID.setIdValue(PRINTER_UUID);
        entityAdditionalIds.add(printerUUID);

        EntityIdentification.EntityAdditionalId printerMCID = new EntityIdentification.EntityAdditionalId();
        printerMCID.setIdType(AdditionalIdType.printer_mcid.name());
        printerMCID.setIdValue(PRINTER_MCID);
        entityAdditionalIds.add(printerMCID);

        EntityIdentification.EntityAdditionalId other = new EntityIdentification.EntityAdditionalId();
        other.setIdType("other-key");
        other.setIdValue("other-value");
        entityAdditionalIds.add(other);

        entityIdentification.setEntityAdditionalIds(entityAdditionalIds);

        entityIdentification.setEntityDomain(ENTITY_DOMAIN);
        entityIdentification.setEntityId(ENTITY_ID);

        List<EntityIdentification.EntityInfo> entityInfo = new ArrayList<EntityIdentification.EntityInfo>();
        EntityIdentification.EntityInfo info = new EntityIdentification.EntityInfo();
        info.setInfoType("info-type");
        info.setInfoValue("info-value");
        entityInfo.add(info);
        entityIdentification.setEntityInfo(entityInfo);

        entityIdentification.setEntityModel(ENTITY_MODEL);
        entityIdentification.setEntityName(ENTITY_NAME);
        entityIdentification.setEntityType(EntityType.devices);

        EntityIdentification.EntityVersion entityVersion = new EntityIdentification.EntityVersion();
        entityVersion.setRevision(REVISION);
        entityVersion.setDate(DATE);
        entityIdentification.setEntityVersion(entityVersion);

        entityIdentification.setOriginator(Originator.sw.value());
        entityIdentification.setResetCounter(0);
        entityIdentification.setVersion(SPEC_VERSION);

        EntityIdentificationBO entityIdentificationBO = BeanConverterUtil.createAndValidateEntityIdentifierBO(entityIdentification, entityTypeProcessorMap);

        assertEquals(entityIdentificationBO.getCountryAndRegionName(), Country.unitedStates.getValue());
        assertFalse(entityIdentificationBO.getEntityAdditionalIds().contains(SSN_VALUE));
        assertTrue(entityIdentificationBO.getEntityAdditionalIds().contains("other-value"));
        assertEquals(entityIdentificationBO.getEntityDomain(), ENTITY_DOMAIN);
        assertEquals(entityIdentificationBO.getEntityId(), ENTITY_ID);
        assertTrue(entityIdentificationBO.getEntityInfo().contains("info-value"));
        assertEquals(entityIdentificationBO.getEntityModel(), ENTITY_MODEL);
        assertEquals(entityIdentificationBO.getEntityName(), ENTITY_NAME);
        assertEquals(entityIdentificationBO.getEntityRevision(), REVISION);
        assertEquals(entityIdentificationBO.getEntityUUID(), PRINTER_UUID);
        assertEquals(entityIdentificationBO.getEntityMCID(), PRINTER_MCID);
        assertEquals(entityIdentificationBO.getEntityVersionDate(), DATE);
        assertEquals(entityIdentificationBO.getLanguage(), Language.SimplifiedChinese.toString());
        assertEquals(entityIdentificationBO.getOriginator(), Originator.sw.value());
        assertEquals(entityIdentificationBO.getResetCounter(), 0);
        assertEquals(entityIdentificationBO.getSpecVersion(), SPEC_VERSION);
        //assertEquals(entityIdentificationBO.getSSN(), SSN_VALUE);

    }

    @Test
    public void testCreateEntityIdentificationBOwithoutMCID()throws Exception{
        EntityIdentification entityIdentification = new EntityIdentification();
        entityIdentification.setCountryAndRegionName(Country.unitedStates);
        entityIdentification.setLanguage(Language.SimplifiedChinese);

        List<EntityIdentification.EntityAdditionalId> entityAdditionalIds = new ArrayList<EntityIdentification.EntityAdditionalId>();
        EntityIdentification.EntityAdditionalId ssn = new EntityIdentification.EntityAdditionalId();
        ssn.setIdType(AdditionalIdType.ssn.name());
        ssn.setIdValue(SSN_VALUE);
        entityAdditionalIds.add(ssn);

        EntityIdentification.EntityAdditionalId printerUUID = new EntityIdentification.EntityAdditionalId();
        printerUUID.setIdType(AdditionalIdType.printer_uuid.name());
        printerUUID.setIdValue(PRINTER_UUID);
        entityAdditionalIds.add(printerUUID);

        EntityIdentification.EntityAdditionalId other = new EntityIdentification.EntityAdditionalId();
        other.setIdType("other-key");
        other.setIdValue("other-value");
        entityAdditionalIds.add(other);

        entityIdentification.setEntityAdditionalIds(entityAdditionalIds);

        entityIdentification.setEntityDomain(ENTITY_DOMAIN);
        entityIdentification.setEntityId(ENTITY_ID);

        List<EntityIdentification.EntityInfo> entityInfo = new ArrayList<EntityIdentification.EntityInfo>();
        EntityIdentification.EntityInfo info = new EntityIdentification.EntityInfo();
        info.setInfoType("info-type");
        info.setInfoValue("info-value");
        entityInfo.add(info);
        entityIdentification.setEntityInfo(entityInfo);

        entityIdentification.setEntityModel(ENTITY_MODEL);
        entityIdentification.setEntityName(ENTITY_NAME);
        entityIdentification.setEntityType(EntityType.devices);

        EntityIdentification.EntityVersion entityVersion = new EntityIdentification.EntityVersion();
        entityVersion.setRevision(REVISION);
        entityVersion.setDate(DATE);
        entityIdentification.setEntityVersion(entityVersion);

        entityIdentification.setOriginator(Originator.sw.value());
        entityIdentification.setResetCounter(0);
        entityIdentification.setVersion(SPEC_VERSION);

        EntityIdentificationBO entityIdentificationBO = BeanConverterUtil.createAndValidateEntityIdentifierBO(entityIdentification, entityTypeProcessorMap);

        assertEquals(entityIdentificationBO.getCountryAndRegionName(), Country.unitedStates.getValue());
        assertFalse(entityIdentificationBO.getEntityAdditionalIds().contains(SSN_VALUE));
        assertTrue(entityIdentificationBO.getEntityAdditionalIds().contains("other-value"));
        assertEquals(entityIdentificationBO.getEntityDomain(), ENTITY_DOMAIN);
        assertEquals(entityIdentificationBO.getEntityId(), ENTITY_ID);
        assertTrue(entityIdentificationBO.getEntityInfo().contains("info-value"));
        assertEquals(entityIdentificationBO.getEntityModel(), ENTITY_MODEL);
        assertEquals(entityIdentificationBO.getEntityName(), ENTITY_NAME);
        assertEquals(entityIdentificationBO.getEntityRevision(), REVISION);
        assertEquals(entityIdentificationBO.getEntityUUID(), PRINTER_UUID);
        assertNull(entityIdentificationBO.getEntityMCID());
        assertEquals(entityIdentificationBO.getEntityVersionDate(), DATE);
        assertEquals(entityIdentificationBO.getLanguage(), Language.SimplifiedChinese.toString());
        assertEquals(entityIdentificationBO.getOriginator(), Originator.sw.value());
        assertEquals(entityIdentificationBO.getResetCounter(), 0);
        assertEquals(entityIdentificationBO.getSpecVersion(), SPEC_VERSION);
        //assertEquals(entityIdentificationBO.getSSN(), SSN_VALUE);

    }

    @Test
    public void testBOWhenEntityTypeIsService()throws Exception{
        EntityIdentification entityIdentification = new EntityIdentification();
        entityIdentification.setCountryAndRegionName(Country.unitedStates);

        entityIdentification.setEntityDomain(ENTITY_DOMAIN);
        entityIdentification.setEntityId(ENTITY_ID);

        List<EntityIdentification.EntityInfo> entityInfo = new ArrayList<EntityIdentification.EntityInfo>();
        EntityIdentification.EntityInfo info = new EntityIdentification.EntityInfo();
        info.setInfoType("info-type");
        info.setInfoValue("info-value");
        entityInfo.add(info);
        entityIdentification.setEntityInfo(entityInfo);

        entityIdentification.setEntityModel(ENTITY_MODEL);
        entityIdentification.setEntityName(ENTITY_NAME);
        entityIdentification.setEntityType(EntityType.services);

        EntityIdentification.EntityVersion entityVersion = new EntityIdentification.EntityVersion();
        entityVersion.setRevision(REVISION);
        entityIdentification.setEntityVersion(entityVersion );

        entityIdentification.setLanguage(Language.EnglishUS);
        entityIdentification.setOriginator(Originator.sw.value());
        entityIdentification.setResetCounter(0);
        entityIdentification.setVersion(SPEC_VERSION);

        EntityIdentificationBO entityIdentificationBO = BeanConverterUtil.createAndValidateEntityIdentifierBO(entityIdentification, entityTypeProcessorMap);

        assertEquals(entityIdentificationBO.getEntityUUID(), ENTITY_ID);
        assertNull(entityIdentificationBO.getEntityVersionDate());
        //assertNull(entityIdentificationBO.getSSN());
    }

    @Test
    public void testBOWhenEntityTypeIsServiceWithEmptyAditionalIds()throws Exception{
        EntityIdentification entityIdentification = new EntityIdentification();
        entityIdentification.setCountryAndRegionName(Country.unitedStates);

        entityIdentification.setEntityDomain(ENTITY_DOMAIN);
        entityIdentification.setEntityId(ENTITY_ID);

        entityIdentification.setEntityInfo(new ArrayList<EntityIdentification.EntityInfo>());

        entityIdentification.setEntityAdditionalIds(new ArrayList<EntityIdentification.EntityAdditionalId>());

        entityIdentification.setEntityModel(ENTITY_MODEL);
        entityIdentification.setEntityName(ENTITY_NAME);
        entityIdentification.setEntityType(EntityType.services);

        EntityIdentification.EntityVersion entityVersion = new EntityIdentification.EntityVersion();
        entityVersion.setRevision(REVISION);
        entityIdentification.setEntityVersion(entityVersion);

        entityIdentification.setLanguage(Language.EnglishUS);
        entityIdentification.setOriginator(Originator.sw.value());
        entityIdentification.setResetCounter(0);
        entityIdentification.setVersion(SPEC_VERSION);

        EntityIdentificationBO entityIdentificationBO = BeanConverterUtil.createAndValidateEntityIdentifierBO(entityIdentification, entityTypeProcessorMap);

        assertEquals(entityIdentificationBO.getEntityUUID(), ENTITY_ID);
        assertNull(entityIdentificationBO.getEntityVersionDate());
        //assertNull(entityIdentificationBO.getSSN());
    }
}
