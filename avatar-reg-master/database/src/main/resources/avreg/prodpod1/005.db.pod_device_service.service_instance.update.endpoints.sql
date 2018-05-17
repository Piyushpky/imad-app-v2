--liquibase formatted sql

--changeset srikanth:005.db.pod_device_service.service_instance.update.endpoints-1
update pod_service.service_instance set service_url='https://connectivity.pod1.avatar.ext.hp.com:443/avatar/v1/entities/connectivityconfig/%s' where service_type='connectivity_config';

--changeset srikanth:005.db.pod_device_service.service_instance.update.endpoints-2
update pod_device.service_instance set service_url='https://connectivity.pod1.avatar.ext.hp.com:443/avatar/v1/entities/connectivityconfig/%s' where service_type='connectivity_config';

--changeset srikanth:005.db.pod_device_service.service_instance.update.endpoints-3
update pod_device.service_instance set service_url='https://registration.pod1.avatar.ext.hp.com:443/avatar/v1/entities/credentials/%s' where service_type='credential_refresh';

--changeset srikanth:005.db.pod_device_service.service_instance.update.endpoints-4
update pod_service.service_instance set service_url='https://registration.pod1.avatar.ext.hp.com:443/avatar/v1/entities/credentials/%s' where service_type='credential_refresh';