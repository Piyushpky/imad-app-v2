--liquibase formatted sql

--changeset potana:001.db.pod_devie.service_instance.changelog.xml-1
INSERT IGNORE INTO pod_device.service_instance VALUES(1,'connectivity_config','https://connectivity.pod1.hp-avatar.com:443/avatar/v1/entities/connectivityconfig/%s','1.0');

--changeset potana:001.db.pod_devie.service_instance.changelog.xml-2
INSERT IGNORE INTO pod_device.service_instance VALUES(2,'printer_caps','null','1.0');