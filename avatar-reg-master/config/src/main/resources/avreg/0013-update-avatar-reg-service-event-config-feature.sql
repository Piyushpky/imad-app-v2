-- liquibase formatted sql
--changeset parsh:0013-update-avatar-reg-service-event-config-feature-1
UPDATE application_config SET config_value = '1.1.0' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';



--changeset parsh:0013-update-avatar-reg-service-event-config-feature-2
UPDATE application_config SET config_value = 'false',version='1.1.0' where config_key = 'avatar-reg.is.event.enabled' AND service_name = 'AVATAR-REG';

