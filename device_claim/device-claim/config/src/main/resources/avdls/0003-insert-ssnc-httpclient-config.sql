-- liquibase formatted sql
--changeset skpu:0003-insert-ssnc-httpclient-config-1
UPDATE application_config SET config_value = '1.0.2' where config_key = 'META_AVATAR-LOOKUP-SERVICE_VERSION' AND service_name = 'AVATAR-LOOKUP-SERVICE';

--changeset arshiya:0003-insert-ssnc-httpclient-config-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES
('avdis.http.socket.timeout.in.millis', '60000', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.2','AVATAR-LOOKUP-SERVICE'),
('avdis.http.max.total.connections', '100', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.2','AVATAR-LOOKUP-SERVICE'),
('avdis.http.max.connections.per.route', '20', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.2','AVATAR-LOOKUP-SERVICE'),
('avdis.http.proxy.enabled', 'false', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.2','AVATAR-LOOKUP-SERVICE'),
('avdis.http.proxy.host', 'web-proxy.cup.hp.com', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.2','AVATAR-LOOKUP-SERVICE'),
('avdis.http.proxy.port', '8080', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.2','AVATAR-LOOKUP-SERVICE'),
('avdis.http.disable.hostname.verification', 'true', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.2','AVATAR-LOOKUP-SERVICE');