--liquibase formatted sql

--changeset pulla:006.db.pod_device.service_instance.update.changelog.xml-1
UPDATE pod_device.service_instance SET service_url='http://pie-pod1-vpreg-Pod-lb-852931012.us-west-2.elb.amazonaws.com/virtualprinter/v1/printers/%s/printerconfig' WHERE service_type='printer_caps';