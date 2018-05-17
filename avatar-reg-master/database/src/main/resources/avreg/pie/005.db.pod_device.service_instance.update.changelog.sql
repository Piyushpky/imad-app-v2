--liquibase formatted sql

--changeset potana:005.db.pod_device.service_instance.update.changelog.xml-1
UPDATE pod_device.service_instance SET service_url='http://54.69.60.47:8080/virtualprinter/v1/printers/%s/printerconfig' WHERE service_type='printer_caps';