-- liquibase formatted sql
--changeset hussain:0007-update-avatar-reg-service-refresh-url-enable-1
UPDATE application_config SET config_value = '1.0.5' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

-- postcard db related configurations

--changeset hussain:0007-update-avatar-reg-service-refresh-url-enable-2
UPDATE application_config SET config_value = 'true' , version = '1.0.5' where config_key = 'avatar-reg.response.credential.refresh.enabled';