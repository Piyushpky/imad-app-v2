INSERT IGNORE INTO application_config (config_key, config_value, scope, identifier, version, service_name) VALUES 
('META_AVATAR-REG_VERSION', '1.0.0', 'METAPROP','NONE','NONE','AVATAR-REG'),


-- service related appconfig entries

('svc_connectivity', 'c3ZjX2Nvbm5lY3Rpdml0eTphdmF0YXI=', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.prod.stage.environment.name','(PROD|STAGE)', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.environment.name','ENVIRONMENT_NAME', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.avatar.application.id','svc_avatar', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),


-- ssn db related configurations

('avatar-reg.ssn.db.driver.class', 'com.mysql.jdbc.Driver', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.dialect.class', 'org.hibernate.dialect.MySQL5InnoDBDialect', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.max.pool.size', '10', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.min.pool.size', '5', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.max.statements', '100', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.max.idle.time', '25200', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.max.idle.time.excess.connections', '6', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.checkout.timeout', '300000', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.acquire.increment', '3', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.acquire.retry.attempt', '60', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.acquire.retry.delay', '5000', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.num.helper.threads', '3', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.test.connection.on.checkout', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.test.connection.on.checkin', 'true', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.idle.connection.test.period', '120', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.preferred.test.query', 'select 1;', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.show.sql', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.ssn.db.generate.ddl', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),

-- postcard db related configurations

('postcard.db.driver.class', 'com.mysql.jdbc.Driver', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.dialect.class', 'org.hibernate.dialect.MySQL5InnoDBDialect', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.max.pool.size', '10', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.min.pool.size', '5', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.max.statements', '100', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.max.idle.time', '25200', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.max.idle.time.excess.connections', '6', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.checkout.timeout', '300000', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.acquire.increment', '3', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.acquire.retry.attempt', '60', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.acquire.retry.delay', '5000', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.num.helper.threads', '3', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.test.connection.on.checkout', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.test.connection.on.checkin', 'true', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.idle.connection.test.period', '120', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.preferred.test.query', 'select 1;', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.show.sql', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.db.generate.ddl', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),


('postcard.ask.length', '16', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.dk.iteration.count', '1033', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.dk.length', '48', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.mek.length', '16', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.mak.length', '32', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.id.length', '32', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.iv.length', '16', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.rsassa_pss.salt.length', '16', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('postcard.is.entity.seq.num.validation.required','false','SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),

-- reg-service db related configurations

('avatar-reg.reg-service.db.driver.class', 'com.mysql.jdbc.Driver', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.dialect.class', 'org.hibernate.dialect.MySQL5InnoDBDialect', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.max.pool.size', '10', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.min.pool.size', '5', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.max.statements', '100', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.max.idle.time', '25200', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.max.idle.time.excess.connections', '6', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.checkout.timeout', '300000', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.acquire.increment', '3', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.acquire.retry.attempt', '60', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.acquire.retry.delay', '5000', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.num.helper.threads', '3', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.test.connection.on.checkout', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.test.connection.on.checkin', 'true', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.idle.connection.test.period', '120', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.preferred.test.query', 'select 1;', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.show.sql', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-service.db.generate.ddl', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),

-- reg-device db related configurations

('avatar-reg.reg-device.db.driver.class', 'com.mysql.jdbc.Driver', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.dialect.class', 'org.hibernate.dialect.MySQL5InnoDBDialect', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.max.pool.size', '10', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.min.pool.size', '5', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.max.statements', '100', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.max.idle.time', '25200', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.max.idle.time.excess.connections', '6', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.checkout.timeout', '300000', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.acquire.increment', '3', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.acquire.retry.attempt', '60', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.acquire.retry.delay', '5000', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.num.helper.threads', '3', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.test.connection.on.checkout', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.test.connection.on.checkin', 'true', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.idle.connection.test.period', '120', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.preferred.test.query', 'select 1;', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.show.sql', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG'),
('avatar-reg.reg-device.db.generate.ddl', 'false', 'SERVICE','AVATAR-REG','1.0.0','AVATAR-REG');