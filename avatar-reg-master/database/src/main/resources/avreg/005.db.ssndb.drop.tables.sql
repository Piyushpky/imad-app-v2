--liquibase formatted sql

--changeset potana:005.db.ssndb.drop.tables-001

DROP DATABASE IF EXISTS ssndb;

--changeset potana:005.db.ssndb.drop.tables-002

create schema IF NOT EXISTS ssndb DEFAULT CHARACTER SET utf8;
