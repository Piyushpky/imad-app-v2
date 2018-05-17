--liquibase formatted sql
--changeset aroraja:avreg_db_pod_device_blacklistrules
CREATE TABLE IF NOT EXISTS pod_device.printer_blacklist_rules
(    
                ruleId BIGINT NOT NULL AUTO_INCREMENT,
                ruleType VARCHAR(50) NOT NULL,
				ruleValue VARCHAR(200) NOT NULL,
				isActive TINYINT NOT NULL,
				created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'stores when ask created/updated timestamp ',
                PRIMARY KEY(ruleId)              				
);	