--liquibase formatted sql

--changeset bnshr:012.db.pod.device.update.registration_domain.Yellowtail_Bluefin.changelog.xml-1
UPDATE pod_device.registration_domain SET entity_domain='Yellowtail_2017_33' WHERE domain_index='33';



--changeset bnshr:012.db.pod.device.update.registration_domain.Yellowtail_Bluefin.changelog.xml-2
UPDATE pod_device.registration_domain SET entity_domain='Bluefin_2017_34' WHERE domain_index='34';
