--liquibase formatted sql

--changeset potana:007.db.pod_device.service_instance.insert.cred.refresh.sql
INSERT IGNORE INTO pod_device.service_instance VALUES(3,'credential_refresh','https://registration.pod1.hp-avatar.com:443/avatar/v1/entities/credentials/%s','1.0');