-- liquibase formatted sql
--changeset srikanth:008-delete-avatar-reg-feature-flags-cleanup-1
UPDATE application_config SET config_value = '1.0.6' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

--changeset srikanth:008-delete-avatar-reg-feature-flags-cleanup-2
DELETE from application_config where config_key = 'avatar-reg.response.credential.refresh.enabled';

--changeset srikanth:008-delete-avatar-reg-feature-flags-cleanup-3
DELETE from application_config where config_key = 'postcard.is.entity.seq.num.validation.required';