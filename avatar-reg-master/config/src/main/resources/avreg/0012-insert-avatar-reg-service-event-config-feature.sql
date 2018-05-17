-- liquibase formatted sql
--changeset parsh:0011-insert-avatar-reg-service-disable-hostname-verification-1
UPDATE application_config SET config_value = '1.0.10' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';



--changeset parsh:0011-insert-avatar-reg-service-disable-hostname-verification-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES
('avatar-reg.is.event.enabled','true', 'SERVICE','AVATAR-REG','1.0.10','AVATAR-REG');
