--liquibase formatted sql

--changeset karanam:0005-insert-ssnc-hystrix-config.sql-1
UPDATE application_config SET config_value = '1.0.4' WHERE config_key = 'META_AVATAR-LOOKUP-SERVICE_VERSION' AND service_name = 'AVATAR-LOOKUP-SERVICE';

--changeset karanam:0005-insert-ssnc-hystrix-config.sql-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES
('hystrix.threadpool.core.size.avdls', '15', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.4','AVATAR-LOOKUP-SERVICE'),
('hystrix.threadpool.maxqueue.size.avdls', '500', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.4','AVATAR-LOOKUP-SERVICE'),
('hystrix.threadpool.queue.rejection.threshold.avdls', '500', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.4','AVATAR-LOOKUP-SERVICE'),
('hystrix.execution.timeout.avdls', '60000', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.4','AVATAR-LOOKUP-SERVICE'),
('hystrix.thread.interruptOnTimeout.avdls', 'false', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.4','AVATAR-LOOKUP-SERVICE'),
('hystrix.fallback.enabled.avdls', 'false', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.4','AVATAR-LOOKUP-SERVICE'),
('hystix.key.name.avdls', 'AvatarLookupService', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.4','AVATAR-LOOKUP-SERVICE');