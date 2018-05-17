--liquibase formatted sql

--changeset potana:011.db.pod_device_service.service_instance.update.end.points.changelog.sql-1
UPDATE pod_service.service_instance SET service_url='https://connectivity.pod1.stage.avatar.ext.hp.com:443/avatar/v1/entities/connectivityconfig/%s' WHERE service_type='connectivity_config';

--changeset potana:011.db.pod_device_service.service_instance.update.end.points.changelog.sql-2
UPDATE pod_device.service_instance SET service_url='https://connectivity.pod1.stage.avatar.ext.hp.com:443/avatar/v1/entities/connectivityconfig/%s' WHERE service_type='connectivity_config';

--changeset potana:011.db.pod_device_service.service_instance.update.end.points.changelog.sql-3
UPDATE pod_service.service_instance SET service_url='https://registration.pod1.stage.avatar.ext.hp.com:443/avatar/v1/entities/credentials/%s' WHERE service_type='credential_refresh';

--changeset potana:011.db.pod_device_service.service_instance.update.end.points.changelog.sql-4
UPDATE pod_device.service_instance SET service_url='https://registration.pod1.stage.avatar.ext.hp.com:443/avatar/v1/entities/credentials/%s' WHERE service_type='credential_refresh';
