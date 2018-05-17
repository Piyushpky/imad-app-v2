CREATE SCHEMA IF NOT EXISTS pod_device;
USE pod_device;
SET NAMES utf8;

CREATE TABLE IF NOT EXISTS entity_device
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	cloud_id CHAR(32) NOT NULL,
	entity_id VARCHAR(64) NOT NULL,
	entity_model VARCHAR(50) NOT NULL,
	entity_uuid VARCHAR(64) NOT NULL,
	entity_name VARCHAR(30),
	reset_counter INTEGER NOT NULL,
	entity_domain VARCHAR(50) NOT NULL,
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'stores when entity created/updated timestamp ',
    PRIMARY KEY (id),
	Unique key UK_entity_id_model(entity_id , entity_model),
	Unique key UK_cloud_id(cloud_id)
);

CREATE TABLE IF NOT EXISTS device_additional_info 
(    
	id BIGINT NOT NULL,
	cloud_id CHAR(32) NOT NULL,
	entity_revision VARCHAR(50) ,
	entity_version_date VARCHAR(20) ,
	country_and_region_name VARCHAR(32) NOT NULL,
	language VARCHAR(10) NOT NULL,
	spec_version VARCHAR(10) NOT NULL,
	originator VARCHAR(50) NOT NULL,
	entity_additional_ids TEXT DEFAULT NULL,
	entity_info TEXT DEFAULT NULL,
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	last_upated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'stores when entity created/updated timestamp ',
	PRIMARY KEY(id),
	FOREIGN KEY FK_device_additional_info_id(id) REFERENCES entity_device(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS service_instance
(
	service_instance_id BIGINT NOT NULL AUTO_INCREMENT,
	service_type VARCHAR(50) NOT NULL,
	service_url VARCHAR(256) NOT NULL,
	spec_version VARCHAR(10) NOT NULL,
    PRIMARY KEY(service_instance_id)
);

CREATE TABLE IF NOT EXISTS hashed_entity_identifier
(
	id BIGINT NOT NULL AUTO_INCREMENT,
	hashed_entity_identifier VARCHAR(100) NOT NULL,
	entity_identifier VARCHAR(100) NOT NULL,
    PRIMARY KEY(id)
);



