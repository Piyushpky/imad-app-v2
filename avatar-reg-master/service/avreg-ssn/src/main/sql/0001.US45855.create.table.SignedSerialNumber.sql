create schema IF NOT EXISTS ssndb;
USE ssndb;
SET NAMES utf8;

DROP PROCEDURE IF EXISTS sp_temp;

DELIMITER $$
CREATE DEFINER=avatar@'localhost' PROCEDURE sp_temp()
DETERMINISTIC
BEGIN
DECLARE s VARCHAR(20);

CREATE TABLE IF NOT EXISTS SignedSerialNumber
(
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
	serialNumber VARCHAR(50) NOT NULL,
	issuanceCounter INTEGER NOT NULL,
	overrunBit BIT NOT NULL,
	domainIndex INTEGER NOT NULL,
	instantInk BIT NOT NULL,
	version VARCHAR(5) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE KEY `SerialNumber` (`serialNumber`)
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

show warnings;

END;
$$
DELIMITER ;

CALL sp_temp;
DROP PROCEDURE IF EXISTS sp_temp;