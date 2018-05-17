-- liquibase formatted sql
--changeset skou:0014-update-avatar-reg-service-event-config-enable-feature-1
UPDATE application_config SET config_value = '1.1.1' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';



--changeset skpu:0014-update-avatar-reg-service-event-config-enable-feature-2
UPDATE application_config SET config_value = 'true',version='1.1.1' where config_key = 'avatar-reg.is.event.enabled' AND service_name = 'AVATAR-REG';

