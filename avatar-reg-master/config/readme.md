 Configuration Version Management
Skip to end of metadata

    Created and last modified by Bibhu Prasad Biswal on Nov 18, 2015

Go to start of metadata
Configuration Version Management

Application configuration evolves on its own independently.Services should have the ability to rollback to any configuration at a given point in time.

As a solution to this problem, I propose a simple set of rules and requirements that dictate how version numbers are assigned and incremented.

    Each configuration is associated with a version major.minor.micro 
        MAJOR version when you make incompatible configuration changes,
        MINOR version when you add new Key
        MICRO version when you patch some value.

    Configurations are immutable
        Any update on value should be a new entry with MICRO version updated.
        Any New Key should be a new entry with MINOR version updated.
        Any update on key should be a new entry with MAJOR version updated.

     3. if a changeset contains a new key and a existing key with new value, then the MINOR version should be changed and the MICRO version should  be reset.

     4. META_<SERVICE>_VERSION is the key used by the configuration service to define the configuration version used by the particular service.

     5. If  META_<SERVICE>_VERSION is set to 1.2.1 the all the properties having version >=1.2.1 is pulled.

Note: Make sure meta-version and property version shoule be created same per sql file.

 
Example

For example if below are the sample change set that went into production, then the property value for different configuration is as below
changeset-1

INSERT INTO `application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('META_WORKER_VERSION', '1.0.0', 'SERVICE', 'METAPROP', '1.0.0', 'WORKER', '2015-10-27 15:48:49');

INSERT INTO `application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('K1', 'V1', 'SERVICE', 'WORKER', '1.0.0', 'WORKER', '2015-10-27 15:48:49');

INSERT INTO `application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('K2', 'V2', 'SERVICE', 'WORKER', '1.0.0', 'WORKER', '2015-10-27 15:48:49');

INSERT INTO `application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('K3', 'V3', 'SERVICE', 'WORKER', '1.0.0', 'WORKER', '2015-10-27 15:48:49');


changeset-2

UPDATE`application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('META_WORKER_VERSION', '1.1.0', 'SERVICE', 'METAPROP', '1.0.0', 'WORKER', '2015-10-27 15:48:49');

INSERT INTO `application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('K4', 'V4', 'SERVICE', 'WORKER', '1.1.0', 'WORKER', '2015-10-27 15:48:49');


changeset-3

UPDATE`application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('META_WORKER_VERSION', '1.1.1', 'SERVICE', 'WORKER', '1.0.0', 'WORKER', '2015-10-27 15:48:49');

INSERT INTO `application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('K1', 'V11', 'SERVICE', 'WORKER', '1.1.1', 'WORKER', '2015-10-27 15:48:49');

changeset -4

UPDATE`application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('META_WORKER_VERSION', '1.2.0', 'SERVICE', 'WORKER', '1.0.0', 'WORKER', '2015-10-27 15:48:49');

INSERT INTO `application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('K1', 'V12', 'SERVICE', 'WORKER', '1.2.0', 'WORKER', '2015-10-27 15:48:49');

INSERT INTO `application_config` (`config_key`, `config_value`, `scope`, `identifier`, `version`, `service_name`, `datecreated`) VALUES ('K5', 'V5', 'SERVICE', 'WORKER', '1.2.0', 'WORKER', '2015-10-27 15:48:49');