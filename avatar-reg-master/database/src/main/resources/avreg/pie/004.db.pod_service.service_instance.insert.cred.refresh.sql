--liquibase formatted sql

--changeset potana:008.db.pod_service.service_instance.insert.cred.refresh.sql
INSERT IGNORE INTO pod_service.service_instance VALUES(3,'credential_refresh','https://pie-pod1-AvatarReg-Pod-lb-891012494.us-west-2.elb.amazonaws.com:443/avatar/v1/entities/credentials/%s','1.0');