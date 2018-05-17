create schema IF NOT EXISTS postcarddb;
USE postcarddb;
SET NAMES utf8;

DROP PROCEDURE IF EXISTS sp_temp;

DELIMITER $$
CREATE DEFINER=avatar@'localhost' PROCEDURE sp_temp()
DETERMINISTIC
BEGIN
DECLARE s VARCHAR(20);

CREATE TABLE IF NOT EXISTS postcard
(
	postcard_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	entity_id VARCHAR(40) NOT NULL COMMENT 'stores entity uuid',
	key_id VARCHAR(32) NOT NULL COMMENT 'stores key_id for secret generation',
	shared_secret VARBINARY(32) NOT NULL COMMENT 'encrypted shared secret.',
	key_agreement_scheme VARCHAR(10) COMMENT 'stores key agreement scheme used during key negotiation.',
	epoc_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'stores when secret created/updated timestamp ',
	PRIMARY KEY (postcard_id),
	UNIQUE KEY UK_postcard_entity_id(entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'table for postcard keys for entity';

CREATE TABLE IF NOT EXISTS postcard_additional_info
(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	postcard_id BIGINT UNSIGNED NOT NULL COMMENT 'Auto generated postcard id reference of postcard',
	application_id VARCHAR(20) NOT NULL COMMENT 'stores application id',
	entity_seq_num BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'entity sequence number',
	service_seq_num BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'service sequence number',
	entity_message_id VARCHAR(64) COMMENT 'entity message id with which postcard received',
	service_message_id VARCHAR(64) COMMENT 'service message id with which postcard constructed',
	entity_signature_hash VARCHAR(512) COMMENT 'entity signature hash with which postcard received',
	service_signature_hash VARCHAR(512) COMMENT 'service signature hash with which postcard constructed',
	entity_instruction VARCHAR(1024) COMMENT 'entity instruction received in the postcard',
	epoc_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'timestamp',
	PRIMARY KEY (id),
	UNIQUE KEY UK_postcard_additional_info_id(postcard_id, application_id),
	FOREIGN KEY FK_postcard_additional_info_id(postcard_id) REFERENCES postcard(postcard_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'table for postcard_additional_info of entity and service';

CREATE TABLE IF NOT EXISTS postcard_renegotiation_info
(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	postcard_id BIGINT UNSIGNED NOT NULL COMMENT 'Auto generated postcard id reference of postcard',
	application_id VARCHAR(20) NOT NULL COMMENT 'stores application id',
	credential_refresh_info BLOB NOT NULL COMMENT 'credential refresh info payload received',
	epoc_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'timestamp',
	PRIMARY KEY (id),
	UNIQUE KEY UK_postcard_renegotiation_info_id(id, postcard_id, application_id),
	FOREIGN KEY FK_postcard_renegotiation_info_id(postcard_id) REFERENCES postcard(postcard_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'table for postcard_additional_info of entity and service';

CREATE TABLE IF NOT EXISTS postcard_certificate_info
(
	id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	certificate_serial_number VARCHAR(20) NOT NULL COMMENT 'server or entity certificate public key serial number in hex format',
	certificate_data BLOB NOT NULL COMMENT 'encrypted certificate in PEM format',
	certificate_owner VARCHAR(20) COMMENT 'owner of certificare server or entity',
	description VARCHAR(20) COMMENT 'descrption about certificate.',
	epoc_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'timestamp',
	PRIMARY KEY (id),
	UNIQUE KEY UK_postcard_certificate_info_id(id, certificate_serial_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'table for postcard_certificate_info of entity and service';

show warnings;

END;
$$
DELIMITER ;

CALL sp_temp;
DROP PROCEDURE IF EXISTS sp_temp;