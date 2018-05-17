--liquibase formatted sql

--changeset jatin:015.db.pod_device.insert.blacklist.sql-1
INSERT INTO printer_blacklist_rules VALUES(1,'RULETYPE_MODEL','K1234A|*',1,'2017-10-30 10:05:53','2017-10-30 10:05:53');

--changeset jatin:015.db.pod_device.insert.blacklist.sql-2
INSERT INTO printer_blacklist_rules VALUES(2,'RULETYPE_MODEL_AND_FIRMWARE_VERSION','K1234B|ABCDE1.2',1,'2017-10-30 10:05:53','2017-10-30 10:05:53');

--changeset jatin:015.db.pod_device.insert.blacklist.sql-3
INSERT INTO printer_blacklist_rules VALUES(3,'RULETYPE_FIRMWARE_VERSION','*|ABCDE1.1',1,'2017-10-30 10:05:53','2017-10-30 10:05:53');