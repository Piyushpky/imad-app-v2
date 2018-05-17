package com.hp.wpp.avatar.restapp.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.wpp.avatar.framework.exceptions.EntityRegistrationNonRetriableException;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.framework.processor.data.RegisteredEntityBO;
import com.hp.wpp.avatar.restapp.common.config.AvatarApplicationConfig;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.Postcard;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * Created by aroraja on 6/8/2017.
 */
@Component
public class ConnectivityAuthValidator {

    private static final WPPLogger LOG = WPPLoggerFactory.getLogger(ConnectivityAuthValidator.class);

    @Autowired
    private AvatarApplicationConfig avatarApplicationConfig;

    @Autowired
    private Postcard postcard;

    @Autowired
    private ValidationHelper validationHelper;

    public void setValidationHelper(ValidationHelper validationHelper) {
        this.validationHelper = validationHelper;
    }

    public void setPostcard(Postcard postcard) {
        this.postcard = postcard;
    }



    public void setAvatarApplicationConfig(
            AvatarApplicationConfig avatarApplicationConfig) {
        this.avatarApplicationConfig = avatarApplicationConfig;
    }



    public RegisteredEntityBO validateConnectivityCustomAuthHeader(String customAuth, String applicationId)  throws EntityRegistrationNonRetriableException {
        String[] authParts = customAuth.split("\\s+");
        String authInfo = authParts[1];

        String decodedAuth = new String(Base64.decodeBase64(authInfo));
        String auth[] = decodedAuth.split(":");
        if(auth.length != 2)
            throw new EntityValidationException("Custom Auth Header do not have expected no.of fields");

        String cloudId= auth[0];
        String entityKey = auth[1];


        RegisteredEntityBO registeredEntity = validationHelper.validateCloudId(cloudId);

        try {
            boolean validateKey = postcard.isValidKey(cloudId, registeredEntity.getEntityUUID(), applicationId, entityKey);
            if(!validateKey)
                throw new EntityValidationException("Postcard validation failed for cloud-id = "+cloudId);

        } catch (PostcardNonRetriableException e) {
        	throw new EntityValidationException("Postcard exception while validating request for cloud-id ="+cloudId,e);
        }

        return registeredEntity;
    }

}
