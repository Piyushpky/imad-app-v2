--liquibase formatted sql

--changeset srikanth:006.db.pod_device.service_instance.update.endpoints-1
update pod_device.service_instance set service_url='https://deviceconfig.pod1.avatar.ext.hp.com/virtualprinter/v1/printers/printerconfig/%s' where service_type='printer_caps';

