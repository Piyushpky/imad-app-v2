create schema IF NOT EXISTS ssndb;
USE ssndb;
SET NAMES utf8;

DROP PROCEDURE IF EXISTS sp_temp;
DELIMITER $$
CREATE DEFINER=avatar@'localhost' PROCEDURE sp_temp()
DETERMINISTIC
BEGIN
DECLARE s VARCHAR(20);

CREATE TABLE IF NOT EXISTS PrinterRegistrationDomain
(
	domainID BIGINT NOT NULL AUTO_INCREMENT,
	registrationDomain VARCHAR(127) NOT NULL,
	SSNIndex INTEGER NOT NULL,
	PRIMARY KEY (domainID),
	UNIQUE KEY UK_PrinterRegistrationDomain_domain(registrationDomain),
	UNIQUE KEY UK_PrinterRegistrationDomain_ssnIndex(SSNIndex)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS PrinterRegistrationDomainKey
(
	domainID BIGINT NOT NULL,
	registrationDomainKey VARCHAR(127) NOT NULL,
	PRIMARY KEY (domainID),
	FOREIGN KEY FK_PrinterRegistrationDomainKey_PrinterRegistrationDomain(domainID) REFERENCES PrinterRegistrationDomain (domainID)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;


show warnings;

END;
$$
DELIMITER ;

CALL sp_temp;
DROP PROCEDURE IF EXISTS sp_temp;