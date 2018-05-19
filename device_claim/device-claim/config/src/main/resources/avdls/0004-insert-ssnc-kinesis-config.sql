-- liquibase formatted sql
--changeset arshiya:0004-insert-ssnc-rls-httpclient-config-1
UPDATE application_config SET config_value = '1.0.3' where config_key = 'META_AVATAR-LOOKUP-SERVICE_VERSION' AND service_name = 'AVATAR-LOOKUP-SERVICE';

--changeset arshiya:0004-insert-ssnc-rls-httpclient-config-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES

('ssnc.kinesis.httpclient.proxy.host', 'web-proxy.cup.hp.com', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.3','AVATAR-LOOKUP-SERVICE'),
('ssnc.kinesis.httpclient.proxy.port', '8080', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.3','AVATAR-LOOKUP-SERVICE'),
('ssnc.kinesis.httpclient.proxy.enabled', 'FALSE', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.3','AVATAR-LOOKUP-SERVICE'),
('ssnc.kinesis.backoff.interval.milisecs', '10000', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.3','AVATAR-LOOKUP-SERVICE'),
('ssnc.kinesis.retry.attempts', '5', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.3','AVATAR-LOOKUP-SERVICE'),
('ssnc.kinesis.check.point.interval.milisecs', '60000', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.3','AVATAR-LOOKUP-SERVICE');
