package com.hp.wpp.avatar.restapp.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.hp.wpp.avatar.framework.exceptions.ModellingException;
import com.hp.wpp.avatar.framework.processor.data.EntityIdentificationBO;
import com.hp.wpp.avatar.restapp.processor.EntityTypeProcessorMap;
import com.hp.wpp.avatar.restmodel.enums.AdditionalIdType;
import com.hp.wpp.avatar.restmodel.enums.Country;
import com.hp.wpp.avatar.restmodel.enums.Language;
import com.hp.wpp.avatar.restmodel.json.schema.EntityIdentification;
import com.hp.wpp.avatar.restmodel.json.schema.EntityInformation;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

public class BeanConverterUtil {

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(BeanConverterUtil.class);

    public static EntityIdentificationBO createAndValidateEntityIdentifierBO(EntityIdentification entityIdentification, EntityTypeProcessorMap entityTypeProcessorMap) {
        EntityIdentificationBO entityIdentificationBO = new EntityIdentificationBO();

        try {
            //Create EntityIdentification BO
            //languageCode would be used to translate enum "Language" to BO's String.
            BeanUtils.copyProperties(entityIdentificationBO, entityIdentification);

            entityIdentificationBO.setCountryAndRegionName(entityIdentification.getCountryAndRegionName().getValue());

            updateEntityIdentificationBOWithVersion(entityIdentification, entityIdentificationBO);

            entityIdentificationBO.setSpecVersion(entityIdentification.getVersion());

            entityIdentificationBO.setHostIP(entityIdentification.getHostIP());

            parseAndUpdateEntityIdentificationBOWithAdditionalIds(entityIdentification, entityIdentificationBO);

            updateEntityIdentificaionBOWithInfoList(entityIdentification, entityIdentificationBO);

            //Validate Entity Identification BO
            (entityTypeProcessorMap.getEntityRegistrationProcessor(entityIdentification.getEntityType())).validateEntityIdentificationBO(entityIdentificationBO);

            if(entityIdentificationBO.getEntityUUID()==null)
                entityIdentificationBO.setEntityUUID(entityIdentification.getEntityId());
            
        } catch (Exception e) {
            throw new ModellingException("Error in createAndValidateEntityIdentifierBO",e);
        }

        return entityIdentificationBO;
    }

    private static void parseAndUpdateEntityIdentificationBOWithAdditionalIds(EntityIdentification entityIdentification, EntityIdentificationBO entityIdentificationBO) {
        List<EntityIdentification.EntityAdditionalId> entityAdditionalIds = entityIdentification.getEntityAdditionalIds();

        if (entityAdditionalIds != null && !entityAdditionalIds.isEmpty()) {
            List<EntityIdentification.EntityAdditionalId> tempAdditionalIds = new ArrayList<EntityIdentification.EntityAdditionalId>(entityAdditionalIds.size());
            for (EntityIdentification.EntityAdditionalId additionalId : entityAdditionalIds) {
                try {
                    switch (AdditionalIdType.valueOf(additionalId.getIdType())) {
                        /*case ssn:
                            entityIdentificationBO.setSSN(additionalId.getIdValue());
                            break;*/

                        case printer_uuid:
                            entityIdentificationBO.setEntityUUID(additionalId.getIdValue());
                            break;

                        case printer_mcid:
                            entityIdentificationBO.setEntityMCID(additionalId.getIdValue());
                            break;

                    }
                } catch (IllegalArgumentException e) {
                    LOG.info("additional-ids  received with non-predefined type : {}", additionalId.getIdType());
                    tempAdditionalIds.add(additionalId);

                }
            }

            if (!tempAdditionalIds.isEmpty()) {
                String additionalIdJson = JSONUtility.marshal(tempAdditionalIds);
                entityIdentificationBO.setEntityAdditionalIds(additionalIdJson);
            }
        }
    }

    private static void updateEntityIdentificaionBOWithInfoList(EntityIdentification entityIdentification, EntityIdentificationBO entityIdentificationBO) {
        List<EntityIdentification.EntityInfo> entityInfoList = entityIdentification.getEntityInfo();
        if(entityInfoList!=null && !entityInfoList.isEmpty()){
            String entityInfoJson = JSONUtility.marshal(entityInfoList);
            entityIdentificationBO.setEntityInfo(entityInfoJson);
        }
    }

    private static void updateEntityIdentificationBOWithVersion(EntityIdentification entityIdentification, EntityIdentificationBO entityIdentificationBO) {
        EntityIdentification.EntityVersion entityVersion = entityIdentification.getEntityVersion();
        entityIdentificationBO.setEntityRevision((entityVersion.getRevision()!=null ? entityVersion.getRevision():null));
        entityIdentificationBO.setEntityVersionDate((entityVersion.getDate() != null ? entityVersion.getDate() : null));
    }

    public static EntityInformation createEntityInformationFromBO(EntityIdentificationBO entityIdentificationBO) {
        EntityInformation entityInformation = new EntityInformation();

            org.springframework.beans.BeanUtils.copyProperties(entityIdentificationBO, entityInformation);
            entityInformation.setCountryAndRegionName(Enum.valueOf(Country.class, entityIdentificationBO.getCountryAndRegionName()));
            entityInformation.setLanguage(Language.getLanguage(entityIdentificationBO.getLanguage()));
            entityInformation.setVersion(entityIdentificationBO.getSpecVersion());

            EntityInformation.EntityVersion entityVersion = new EntityInformation.EntityVersion();
            entityVersion.setRevision(entityIdentificationBO.getEntityRevision());
            entityVersion.setDate(entityIdentificationBO.getEntityVersionDate());
            entityInformation.setEntityVersion(entityVersion);

            if(entityIdentificationBO.getEntityUUID() != null) {
                EntityInformation.EntityAdditionalId entityAdditionalId = new EntityInformation.EntityAdditionalId();
                entityAdditionalId.setIdType(AdditionalIdType.printer_uuid.name());
                entityAdditionalId.setIdValue(entityIdentificationBO.getEntityUUID());


                List<EntityInformation.EntityAdditionalId> addtionalIds = new ArrayList<>();
                addtionalIds.add(entityAdditionalId);
                entityInformation.setEntityAdditionalIds(addtionalIds);
            }


        return entityInformation;
    }
}
