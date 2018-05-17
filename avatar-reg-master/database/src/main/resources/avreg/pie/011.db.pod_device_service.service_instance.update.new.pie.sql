--liquibase formatted sql

--changeset srikanth:011.db.pod_device_service.service_instance.update.new.pie.xml-1
UPDATE pod_device.service_instance SET service_url='https://pie-dvc-pod1-connmgmt-podlb-1618051512.us-west-2.elb.amazonaws.com/avatar/v1/entities/connectivityconfig/%s' WHERE service_type='connectivity_config';

--changeset srikanth:011.db.pod_device_service.service_instance.update.new.pie.xml-2
UPDATE pod_device.service_instance SET service_url='https://pie-dvc-pod1-vpreg-podlb-1392744125.us-west-2.elb.amazonaws.com/virtualprinter/v1/printers/printerconfig/%s' WHERE service_type='printer_caps';

--changeset srikanth:011.db.pod_device_service.service_instance.update.new.pie.xml-3
UPDATE pod_device.service_instance SET service_url='https://pie-dvc-pod1-avreg-podlb-349898534.us-west-2.elb.amazonaws.com/avatar/v1/entities/credentials/%s' WHERE service_type='credential_refresh';

--changeset srikanth:011.db.pod_device_service.service_instance.update.new.pie.xml-4
UPDATE pod_service.service_instance SET service_url='https://pie-dvc-pod1-connmgmt-podlb-1618051512.us-west-2.elb.amazonaws.com/avatar/v1/entities/connectivityconfig/%s' WHERE service_type='connectivity_config';

--changeset srikanth:011.db.pod_device_service.service_instance.update.new.pie.xml-5
UPDATE pod_service.service_instance SET service_url='https://pie-dvc-pod1-avreg-podlb-349898534.us-west-2.elb.amazonaws.com/avatar/v1/entities/credentials/%s' WHERE service_type='credential_refresh';
