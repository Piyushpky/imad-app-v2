INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES 
('META_AVATAR-LOOKUP-SERVICE_VERSION', '1.0.0', 'METAPROP','NONE','NONE','AVATAR-LOOKUP-SERVICE'),

-- email address service db related configurations

('ssnc.dynamodb.httpclient.connection.timeout', '20000', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.0','AVATAR-LOOKUP-SERVICE'),
('ssnc.dynamodb.httpclient.max.connections', '100', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.0','AVATAR-LOOKUP-SERVICE'),
('ssnc.dynamodb.httpclient.max.error.retry', '3', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.0','AVATAR-LOOKUP-SERVICE'),
('ssnc.dynamodb.httpclient.proxy.host', 'web-proxy.cup.hp.com', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.0','AVATAR-LOOKUP-SERVICE'),
('ssnc.dynamodb.httpclient.proxy.port', '8080', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.0','AVATAR-LOOKUP-SERVICE'),
('ssnc.dynamodb.httpclient.proxy.enabled', 'FALSE', 'SERVICE','AVATAR-LOOKUP-SERVICE','1.0.0','AVATAR-LOOKUP-SERVICE')
