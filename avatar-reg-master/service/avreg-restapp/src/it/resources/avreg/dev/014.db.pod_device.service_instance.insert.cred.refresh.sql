--liquibase formatted sql

--changeset potana:007.db.pod_device.service_instance.insert.cred.refresh.sql
INSERT  INTO service_instance VALUES(3,'credential_refresh','https://pie-pod1-AvatarReg-Pod-lb-891012494.us-west-2.elb.amazonaws.com:443/avatar/v1/entities/credentials/%s','1.0');