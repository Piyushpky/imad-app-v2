--liquibase formatted sql

--changeset rajrajat:012.db.pod_device.domain_index.update.changelog.sql
UPDATE pod_device.registration_domain SET domain_index=9 WHERE entity_domain='simulator';

