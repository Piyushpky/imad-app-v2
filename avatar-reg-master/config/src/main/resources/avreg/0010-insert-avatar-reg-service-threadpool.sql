-- liquibase formatted sql
--changeset som:0010-insert-avatar-reg-service-threadpool-1
UPDATE application_config SET config_value = '1.0.8' where config_key = 'META_AVATAR-REG_VERSION' AND service_name = 'AVATAR-REG';

-- threadpool related configurations

--changeset som:0010-insert-avatar-reg-service-threadpool-2
INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES 
('avatar-reg.http.socket.timeout.in.millis','10000', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),
('avatar-reg.http.max.total.connections','100', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),
('avatar-reg.http.max.connections.per.route','100', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),
('avatar-reg.http.proxy.enabled','false', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),
('avatar-reg.http.proxy.host','web-proxy.ind.hp.com', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),
('avatar-reg.http.proxy.port','8080', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),

('avatar-reg.thread.pool.core.size','50', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),
('avatar-reg.thread.pool.maximum.size','50', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),
('avatar-reg.thread.pool.keep.alive.time.in.ms','10000', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG'),
('avatar-reg.thread.pool.core.size','100', 'SERVICE','AVATAR-REG','1.0.8','AVATAR-REG');





