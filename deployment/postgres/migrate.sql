-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: src/main/resources/liquibase/db/db.changelog-master.xml
-- Ran at: 10.5.2016 0:29
-- Against: postgres@jdbc:postgresql://localhost:5432/crossbuild
-- Liquibase version: 3.4.2
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE crossbuild.databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITHOUT TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM crossbuild.databasechangeloglock;

INSERT INTO crossbuild.databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
UPDATE crossbuild.databasechangeloglock SET LOCKED = TRUE, LOCKEDBY = 'machine (192.168.2.171)', LOCKGRANTED = '2016-05-10 00:29:59.405' WHERE ID = 1 AND LOCKED = FALSE;

-- Create Database Change Log Table
CREATE TABLE crossbuild.databasechangelog (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED TIMESTAMP WITHOUT TIME ZONE NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONTEXTS VARCHAR(255), LABELS VARCHAR(255));

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::setup_pg_extentions::Klemen
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
	
	          CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('setup_pg_extentions', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 1, '7:8e89d8caae6be0dc662a8a0c996a431a', 'sql', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_container::Klemen
CREATE TABLE crossbuild.containers (name VARCHAR(255) NOT NULL, ip VARCHAR(45) NOT NULL, port INT NOT NULL, vm_id VARCHAR(36) NOT NULL, project_id VARCHAR(36) NOT NULL, keypair_id VARCHAR(36) NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_container', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 2, '7:a9c4bf419405b51cd01f406ab28958e0', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_keypairs::Klemen
CREATE TABLE crossbuild.keypairs (id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL, name VARCHAR(45) NOT NULL, priv_material TEXT NOT NULL, pub_material TEXT NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_keypairs', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 3, '7:d06c07d171ca0d2611091223a7a8710f', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_load_history::Klemen
CREATE TABLE crossbuild.load_history (id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL, type VARCHAR(10) NOT NULL, value DOUBLE PRECISION NOT NULL, vm_id VARCHAR(36) NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_load_history', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 4, '7:25b5e048db13892023b96ed1eb097a74', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_platform::Klemen
CREATE TABLE crossbuild.platforms (id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL, name VARCHAR(45) NOT NULL, kernel VARCHAR(45) NOT NULL, arch VARCHAR(45) NOT NULL, version VARCHAR(45) NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_platform', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 5, '7:256bc61d4b94e84b7e7e8f0df5429856', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_project_groups::Klemen
CREATE TABLE crossbuild.project_groups (id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL, name VARCHAR(255) NOT NULL, repository VARCHAR(2048) NOT NULL, user_id VARCHAR(36) NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_project_groups', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 6, '7:a16cbd71a47b6efdba91e5bb9ba958c6', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_projects::Klemen
CREATE TABLE crossbuild.projects (id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL, build_command TEXT NOT NULL, vm_setting_id VARCHAR(36) NOT NULL, project_group_id VARCHAR(36) NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_projects', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 7, '7:8da4d59dabda6b02054a431045844fee', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_users::Klemen
CREATE TABLE crossbuild.users (id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_users', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 8, '7:1f15898ec0af8975b7ad61167342992b', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_vms::Klemen
CREATE TABLE crossbuild.vms (id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL, provider VARCHAR(45) NOT NULL, cloud_id VARCHAR(255) NOT NULL, group_name VARCHAR(45) NOT NULL, ip VARCHAR(45) NOT NULL, keypair_id VARCHAR(36) NOT NULL, vm_setting_id VARCHAR(36) NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_vms', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 9, '7:48591a4adea11c677970229fbc05a381', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::create_vm_settings::Klemen
CREATE TABLE crossbuild.vm_settings (id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL, group_name VARCHAR(45) NOT NULL, provider VARCHAR(45) NOT NULL, bootstrap_upload_path VARCHAR(255) NOT NULL, login_user VARCHAR(45) NOT NULL, bootstrap_exec_cmd VARCHAR(2048) NOT NULL, ami_id VARCHAR(45) NOT NULL, bootstrap_as_sudo BOOLEAN NOT NULL, container_file VARCHAR(45) NOT NULL, create_container_cmd VARCHAR(2048) NOT NULL, delete_container_cmd VARCHAR(2048) NOT NULL, platform_id VARCHAR(36) NOT NULL);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('create_vm_settings', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 10, '7:876a7f207366f6018327c7262bad656c', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::add_primary_keys::Klemen
ALTER TABLE crossbuild.containers ADD CONSTRAINT pk_containers PRIMARY KEY (project_id);

ALTER TABLE crossbuild.keypairs ADD CONSTRAINT pk_keypairs PRIMARY KEY (id);

ALTER TABLE crossbuild.load_history ADD CONSTRAINT pk_load_history PRIMARY KEY (id);

ALTER TABLE crossbuild.platforms ADD CONSTRAINT pk_platforms PRIMARY KEY (id);

ALTER TABLE crossbuild.projects ADD CONSTRAINT pk_projects PRIMARY KEY (id);

ALTER TABLE crossbuild.project_groups ADD CONSTRAINT pk_project_groups PRIMARY KEY (id);

ALTER TABLE crossbuild.users ADD CONSTRAINT pk_user PRIMARY KEY (id);

ALTER TABLE crossbuild.vms ADD CONSTRAINT pk_vms PRIMARY KEY (id);

ALTER TABLE crossbuild.vm_settings ADD CONSTRAINT pk_vm_settings PRIMARY KEY (id);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('add_primary_keys', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 11, '7:1e8811e32d71a8547fe27d40d21a38d9', 'addPrimaryKey (x9)', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::add_foreign_keys::Klemen
ALTER TABLE crossbuild.containers ADD CONSTRAINT fk_containers_vms FOREIGN KEY (vm_id) REFERENCES crossbuild.vms (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE crossbuild.containers ADD CONSTRAINT fk_containers_projects FOREIGN KEY (project_id) REFERENCES crossbuild.projects (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE crossbuild.containers ADD CONSTRAINT fk_containers_keypairs FOREIGN KEY (keypair_id) REFERENCES crossbuild.keypairs (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE crossbuild.load_history ADD CONSTRAINT fk_load_history_vms FOREIGN KEY (vm_id) REFERENCES crossbuild.vms (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE crossbuild.project_groups ADD CONSTRAINT fk_project_groups_users FOREIGN KEY (user_id) REFERENCES crossbuild.users (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE crossbuild.projects ADD CONSTRAINT fk_project_vmsettings FOREIGN KEY (vm_setting_id) REFERENCES crossbuild.vm_settings (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE crossbuild.projects ADD CONSTRAINT fk_project_project_group FOREIGN KEY (project_group_id) REFERENCES crossbuild.project_groups (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE crossbuild.vms ADD CONSTRAINT fk_vms_keypairs FOREIGN KEY (keypair_id) REFERENCES crossbuild.keypairs (id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE crossbuild.vms ADD CONSTRAINT fk_vms_vm_settings FOREIGN KEY (vm_setting_id) REFERENCES crossbuild.vm_settings (id) ON UPDATE CASCADE ON DELETE CASCADE;

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('add_foreign_keys', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 12, '7:d8f8f9dc14b0e85b07009a034f56da79', 'addForeignKeyConstraint (x9)', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-initial-scheme.xml::add_unique_constraints::Klemen
ALTER TABLE crossbuild.keypairs ADD CONSTRAINT keypair_name_unique UNIQUE (name);

ALTER TABLE crossbuild.project_groups ADD CONSTRAINT project_group_name UNIQUE (name, user_id);

ALTER TABLE crossbuild.containers ADD CONSTRAINT container_name UNIQUE (name);

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('add_unique_constraints', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-initial-scheme.xml', NOW(), 13, '7:0dba28179505df6f2eaf112f8650a36c', 'addUniqueConstraint (x3)', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Changeset src/main/resources/liquibase/db/db.changelog-revision.xml::add_revision::Klemen
ALTER TABLE crossbuild.containers ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.containers ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.keypairs ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.keypairs ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.load_history ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.load_history ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.platforms ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.platforms ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.projects ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.projects ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.project_groups ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.project_groups ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.users ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.users ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.vms ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.vms ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.vm_settings ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE crossbuild.vm_settings ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

INSERT INTO crossbuild.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('add_revision', 'Klemen', 'src/main/resources/liquibase/db/db.changelog-revision.xml', NOW(), 14, '7:7f64792f0c3924010eedabac8107b02f', 'addColumn (x9)', '', 'EXECUTED', NULL, NULL, '3.4.2');

-- Release Database Lock
UPDATE crossbuild.databasechangeloglock SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

