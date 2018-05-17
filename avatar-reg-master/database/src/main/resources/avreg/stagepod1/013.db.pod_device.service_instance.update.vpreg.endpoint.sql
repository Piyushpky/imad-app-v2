--liquibase formatted sql

--changeset saranya:013.db.pod_device.service_instance.update.vpreg.endpoint.sql
UPDATE pod_device.service_instance SET service_url='https://stg-dvc-pod1-vpreg-podlb-1237064160.us-west-2.elb.amazonaws.com:443/virtualprinter/v1/printers/printerconfig/%s' WHERE service_type='printer_caps';
