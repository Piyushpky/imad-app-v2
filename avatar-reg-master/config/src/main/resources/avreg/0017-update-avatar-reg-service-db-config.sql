-- liquibase formatted sql
--changeset sindhu:0017-update-avatar-reg-service-db-config-1
UPDATE application_config SET config_value = '1.1.2' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';



--changeset sindhu:0017-update-avatar-reg-service-db-config-2
UPDATE application_config SET config_value = '18000',version='1.1.2' where config_key = 'postcard.db.acquire.retry.attempt' AND service_name = 'AVATAR-REG';

--changeset sindhu:0017-update-avatar-reg-service-db-config-3
UPDATE application_config SET config_value = '18000',version='1.1.2' where config_key = 'avatar-reg.ssn.db.acquire.retry.attempt' AND service_name = 'AVATAR-REG';

--changeset sindhu:0017-update-avatar-reg-service-db-config-4
UPDATE application_config SET config_value = '60',version='1.1.2' where config_key = 'postcard.db.max.idle.time.excess.connections' AND service_name = 'AVATAR-REG';

--changeset sindhu:0017-update-avatar-reg-service-db-config-5
UPDATE application_config SET config_value = '60',version='1.1.2' where config_key = 'avatar-reg.ssn.db.max.idle.time.excess.connections' AND service_name = 'AVATAR-REG';

--changeset sindhu:0017-update-avatar-reg-service-db-config-6
UPDATE application_config SET config_value = '18000',version='1.1.2' where config_key = 'avatar-reg.reg-service.db.acquire.retry.attempt' AND service_name = 'AVATAR-REG';

--changeset sindhu:0017-update-avatar-reg-service-db-config-7
UPDATE application_config SET config_value = '60',version='1.1.2' where config_key = 'avatar-reg.reg-service.db.max.idle.time.excess.connections' AND service_name = 'AVATAR-REG';
