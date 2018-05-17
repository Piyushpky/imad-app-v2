--liquibase formatted sql

--changeset potana:010db.pod_service.service_instance.update.cred.refresh.sql
UPDATE pod_service.service_instance SET service_url='https://registration.pod1.stage.hp-avatar.com:443/avatar/v1/entities/credentials/%s' WHERE service_type='credential_refresh';

--changeset potana:010.db.pod_device.service_instance.update.cred.refresh.sql
UPDATE pod_device.service_instance SET service_url='https://registration.pod1.stage.hp-avatar.com:443/avatar/v1/entities/credentials/%s' WHERE service_type='credential_refresh';