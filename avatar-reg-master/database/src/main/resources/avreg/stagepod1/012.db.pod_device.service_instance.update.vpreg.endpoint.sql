--liquibase formatted sql

--changeset srikanth:012.db.pod_device.service_instance.update.vpreg.endpoint.sql
UPDATE pod_device.service_instance SET service_url='https://deviceconfig.pod1.stage.avatar.ext.hp.com/virtualprinter/v1/printers/printerconfig/%s' WHERE service_type='printer_caps';
