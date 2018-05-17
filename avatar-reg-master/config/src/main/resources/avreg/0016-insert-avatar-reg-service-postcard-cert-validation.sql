-- liquibase formatted sql
--changeset hussain:0016-insert-avatar-reg-service-postcard-cert-validation-1
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES ('postcard.is.entity.cert.validation.required','false', 'SERVICE','AVATAR-REG','1.1.1','AVATAR-REG');