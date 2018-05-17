-- liquibase formatted sql
--changeset hussain:009-insert-avatar-reg-disable_seq_num-1
UPDATE application_config SET config_value = '1.0.7' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

--changeset hussain:009-insert-avatar-reg-disable_seq_num-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES 
('postcard.is.entity.seq.num.validation.required','false','SERVICE','AVATAR-REG','1.0.7','AVATAR-REG');