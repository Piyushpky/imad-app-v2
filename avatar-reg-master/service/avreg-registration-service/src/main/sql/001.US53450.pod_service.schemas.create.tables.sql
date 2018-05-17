CREATE SCHEMA IF NOT EXISTS pod_service;
USE pod_service;
SET NAMES utf8;

CREATE TABLE IF NOT EXISTS entity_service
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	cloud_id CHAR(32) NOT NULL,
	entity_id VARCHAR(64) NOT NULL,
	entity_model VARCHAR(64) NOT NULL,
	entity_uuid VARCHAR(64) NOT NULL,
	reset_counter INTEGER NOT NULL,
	entity_name VARCHAR(128),
	entity_domain VARCHAR(64) NOT NULL,
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'stores when entity created/updated timestamp ',
    PRIMARY KEY (id),
	Unique key UK_entity_id_model(entity_id , entity_model),
	Unique key UK_cloud_id(cloud_id),
	Unique key UK_entity_uuid(entity_uuid)
);

CREATE TABLE IF NOT EXISTS service_additional_info 
(    
	id BIGINT NOT NULL,
	cloud_id CHAR(32) NOT NULL,
	entity_revision VARCHAR(64) ,
	entity_version_date VARCHAR(32) ,
	country_and_region_name VARCHAR(64) NOT NULL,
	language VARCHAR(8) NOT NULL,
	spec_version VARCHAR(8) NOT NULL,
	originator VARCHAR(32) NOT NULL,
	entity_additional_ids TEXT DEFAULT NULL,
	entity_info TEXT DEFAULT NULL,
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	last_upated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'stores when entity created/updated timestamp ',
	PRIMARY KEY(id),
	FOREIGN KEY FK_service_additional_info_id(id) REFERENCES entity_service(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS service_instance
(
	service_instance_id BIGINT NOT NULL AUTO_INCREMENT,
	service_type VARCHAR(32) NOT NULL,
	service_url VARCHAR(128) NOT NULL,
	spec_version VARCHAR(8) NOT NULL,
    PRIMARY KEY(service_instance_id)
);

CREATE TABLE IF NOT EXISTS hashed_entity_identifier
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	hashed_entity_identifier VARCHAR(128) NOT NULL,
	entity_identifier VARCHAR(64) NOT NULL,
    PRIMARY KEY(id)
);