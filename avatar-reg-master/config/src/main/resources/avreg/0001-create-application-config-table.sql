CREATE TABLE IF NOT EXISTS ops_db.application_config (
	config_key VARCHAR(100) NOT NULL,
	config_value VARCHAR(100) NOT NULL,
	scope VARCHAR(50) NOT NULL,
	identifier VARCHAR(50) NOT NULL,
	version VARCHAR(10) NOT NULL,
	service_name VARCHAR(50) NOT NULL,
	datecreated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UNIQUE INDEX unq_index (config_key, scope, identifier, version)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;