--liquibase formatted sql

--changeset potana:001.db.pod_service.service_instance.changelog.xml-1
INSERT  INTO service_instance VALUES(4,'service_config','null','1.0');