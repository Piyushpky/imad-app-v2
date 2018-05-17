-- liquibase formatted sql
--changeset sindhu:0018-update-avatar-reg-service-db-config-1
UPDATE application_config SET config_value = '1.1.3' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

--changeset sindhu:0018-update-avatar-reg-service-db-config-2
UPDATE application_config SET config_value = '18000',version='1.1.3' where config_key = 'avatar-reg.reg-device.db.acquire.retry.attempt' AND service_name = 'AVATAR-REG';

--changeset sindhu:0018-update-avatar-reg-service-db-config-3
UPDATE application_config SET config_value = '60',version='1.1.3' where config_key = 'avatar-reg.reg-device.db.max.idle.time.excess.connections' AND service_name = 'AVATAR-REG';
