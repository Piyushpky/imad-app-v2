--liquibase formatted sql

--changeset srikanth:006.db.pod_device.service_instance.vpreg..update.endpoint.sql-1
update pod_device.service_instance set service_url='https://prod-dvc-pod1-vpreg-podlb-610831463.us-west-2.elb.amazonaws.com/virtualprinter/v1/printers/printerconfig/%s' where service_type='printer_caps';