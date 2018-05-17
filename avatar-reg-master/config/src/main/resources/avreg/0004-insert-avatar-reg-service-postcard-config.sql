-- liquibase formatted sql
--changeset hussain:0004-insert-avatar-reg-service-postcard-config-1
UPDATE application_config SET config_value = '1.0.2' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

-- postcard db related configurations

--changeset hussain:0004-insert-avatar-reg-service-postcard-config-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES 
('postcard.supported.domain','certificate_entity','SERVICE','AVATAR-REG','1.0.2','AVATAR-REG');