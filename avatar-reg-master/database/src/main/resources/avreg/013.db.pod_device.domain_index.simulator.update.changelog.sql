--liquibase formatted sql

--changeset srikanth:013.db.pod_device.domain_index.simulator.update.changelog.sql
UPDATE pod_device.registration_domain SET entity_domain='PrinterSimulator_2017_9' WHERE domain_id=2;

