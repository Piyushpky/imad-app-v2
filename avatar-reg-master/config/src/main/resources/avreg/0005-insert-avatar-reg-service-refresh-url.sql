-- liquibase formatted sql
--changeset srikanth:0005-insert-avatar-reg-service-refresh-url-1
UPDATE application_config SET config_value = '1.0.3' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

-- postcard db related configurations

--changeset srikanth:0005-insert-avatar-reg-service-refresh-url-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES 
('avatar-reg.response.credential.refresh.enabled','false','SERVICE','AVATAR-REG','1.0.3','AVATAR-REG');