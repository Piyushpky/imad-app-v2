package com.hp.wpp.avatar.restapp.resources;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.avatar.framework.enums.EntityType;
import com.hp.wpp.avatar.framework.exceptions.EntityNotRegisteredException;
import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.processor.EntityRegistrationProcessor;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.avatar.restmodel.enums.Country;
import com.hp.wpp.avatar.restmodel.enums.Language;

public class EntityInfoResourceTest extends BaseResourceTest  {

    private static final String URL = "/v1/entities/entity_info/%s";
    private static final String CLOUD_ID = "seoupthe3pk";
    public static final String ENTITY_INFO_APPI_URL = ROOT_URL + String.format(URL, CLOUD_ID);

    public static final String ENTITY_UUID = "entity_uuid";
    public static final String ORIGINATOR = "ews";
    public static final String ENTITY_INFO = "entity_info";
    public static final Country COUNTRY = Country.unitedStates;
    public static final Language LANGUAGE = Language.EnglishUS;
    private static final String SPEC_VERSION = "1.0";
    private static final String DATE = "2015-01-01";
    private static final String REVISION = "revision";
    private static final String ENTITY_NAME = "entity-name";
    private static final String ENTITY_MODEL = "entity-model";
    private static final String ENTITY_ID = "entity-id";
    private static final String ENTITY_DOMAIN = "entity-domain";

    @InjectMocks
    private EntityInfoResource entityInfoResource;

    @Mock
    private EntityRegistrationProcessor entityRegistrationProcessor;

    @BeforeMethod
    public void setup(){
        MockitoAnnotations.initMocks(this);
        super.setup();

        Map<EntityType, EntityRegistrationProcessor> entityTypeToProcessorMap = new HashMap<>();
        entityTypeToProcessorMap.put(EntityType.devices, entityRegistrationProcessor);
        entityTypeToProcessorMap.put(EntityType.services, entityRegistrationProcessor);

        EntityTypeProcessorMap entityTypeProcessorMap = new EntityTypeProcessorMap();
        entityTypeProcessorMap.setEntityTypeToProcessorMap(entityTypeToProcessorMap);
        entityInfoResource.setEntityTypeProcessorMap(entityTypeProcessorMap);

        try {
            Mockito.when(entityRegistrationProcessor.getEntityIdentificationBO(CLOUD_ID)).thenReturn(getEntityIdentificationBO());
        } catch (EntityRegistrationNonRetriableException e) {
            e.printStackTrace();
        }

    }

    private EntityIdentificationBO getEntityIdentificationBO() {
        EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();
        entityIdentificationBO.setEntityId(ENTITY_ID);
        entityIdentificationBO.setEntityDomain(ENTITY_DOMAIN);
        entityIdentificationBO.setEntityModel(ENTITY_MODEL);
        entityIdentificationBO.setEntityName(ENTITY_NAME);
        entityIdentificationBO.setResetCounter(2);
        entityIdentificationBO.setEntityUUID(ENTITY_UUID);
        entityIdentificationBO.setCountryAndRegionName(COUNTRY.name());
        entityIdentificationBO.setEntityAdditionalIds("entity_additional_ids");
        entityIdentificationBO.setEntityInfo(ENTITY_INFO);
        entityIdentificationBO.setEntityRevision(REVISION);
        entityIdentificationBO.setEntityVersionDate(DATE);
        entityIdentificationBO.setLanguage(LANGUAGE.toString());
        entityIdentificationBO.setOriginator(ORIGINATOR);
        entityIdentificationBO.setSpecVersion(SPEC_VERSION);

        return entityIdentificationBO;
    }

    @Override
    protected Object getResourceClassToBeTested() {
        return entityInfoResource;
    }

    @Test
    public void testEntityInfoResource() throws Exception{
        given().contentType(MediaType.APPLICATION_JSON)
                .get(ENTITY_INFO_APPI_URL)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .and()
                .body("version", equalTo("1.0"))
                .body("entity_type", equalTo("devices"))
                .body("entity_name", equalTo(ENTITY_NAME));
    }

    @DataProvider(name = "invalidScenarios")
    public Object[][] invalidScenarios(){
        return new Object[][]{
                {new EntityNotRegisteredException("cloudId not exist"),Response.Status.NOT_FOUND.getStatusCode()}  ,
                {new RuntimeException(" unknown error"),Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()}
        };
    }
    @Test(dataProvider = "invalidScenarios")
    public void testEntityResourceAPIWIthInvalidScenarios(Exception e,int statusCode) throws  Exception{
        try {
            Mockito.when(entityRegistrationProcessor.getEntityIdentificationBO(CLOUD_ID)).thenThrow(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        invokeRestAPIAndVerifyFailureResponse(statusCode);
    }

    private void invokeRestAPIAndVerifyFailureResponse(int statusCode) {
        given()
                .get(ENTITY_INFO_APPI_URL)
                .then()
                .assertThat().
                statusCode(statusCode);
    }
}
