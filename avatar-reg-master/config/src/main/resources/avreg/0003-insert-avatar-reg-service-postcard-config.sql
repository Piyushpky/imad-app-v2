-- liquibase formatted sql
--changeset hussain:0003-insert-avatar-reg-service-postcard-config-1
UPDATE application_config SET config_value = '1.0.1' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

-- postcard db related configurations

--changeset hussain:0003-insert-avatar-reg-service-postcard-config-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES 
('postcard.keystore.location','/opt/wpp/gen2/certs/java_keystore.jks','SERVICE','AVATAR-REG','1.0.1','AVATAR-REG'),
('postcard.keystore.password','changeit','SERVICE','AVATAR-REG','1.0.1','AVATAR-REG'),
('postcard.sharedsecret.encryption.key','JlUvJMD7cc2AbDvEprm8tg==','SERVICE','AVATAR-REG','1.0.1','AVATAR-REG'),
('postcard.sharedsecret.encryption.iv','aeGmTRJ3TNkEXZhT+iTF6w==','SERVICE','AVATAR-REG','1.0.1','AVATAR-REG');