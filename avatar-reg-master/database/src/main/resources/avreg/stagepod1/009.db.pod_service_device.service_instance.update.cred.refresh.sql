--liquibase formatted sql

--changeset potana:009.db.pod_service.service_instance.update.cred.refresh.sql
UPDATE pod_service.service_instance SET service_url='https://registration.stage.pod1.hp-avatar.com:443/avatar/v1/entities/credentials/%s' WHERE service_type='credential_refresh';

--changeset potana:009.db.pod_device.service_instance.update.cred.refresh.sql
UPDATE pod_device.service_instance SET service_url='https://registration.stage.pod1.hp-avatar.com:443/avatar/v1/entities/credentials/%s' WHERE service_type='credential_refresh';