--liquibase formatted sql

--changeset potana:006.db.pod_service.service_instance.update.changelog.xml-1
UPDATE pod_service.service_instance SET service_url='https://connectivity.pod1.stage.hp-avatar.com:443/avatar/v1/entities/connectivityconfig/%s' WHERE service_type='connectivity_config';