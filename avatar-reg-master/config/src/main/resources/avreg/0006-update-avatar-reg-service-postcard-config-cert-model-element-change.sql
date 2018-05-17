-- liquibase formatted sql
--changeset hussain:0006-update-avatar-reg-service-postcard-config-cert-model-element-change-1
UPDATE application_config SET config_value = '1.0.4' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

-- postcard db related configurations

--changeset hussain:0006-update-avatar-reg-service-postcard-config-cert-model-element-change-2
UPDATE application_config SET config_value = 'certificate_model', version = '1.0.4' where config_key = 'postcard.supported.domain';