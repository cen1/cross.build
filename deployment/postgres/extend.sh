#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	CREATE DATABASE crossbuild;
	CREATE DATABASE keycloak;
	CREATE USER keycloak WITH ENCRYPTED PASSWORD 'keycloak';
	GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname crossbuild <<-EOSQL
    CREATE USER crossbuild WITH ENCRYPTED PASSWORD 'crossbuild';
	GRANT ALL PRIVILEGES ON DATABASE crossbuild TO crossbuild;
	CREATE SCHEMA crossbuild;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname crossbuild < /tmp/migrate.sql

echo "host all  all    172.17.0.1/16  md5" >> /var/lib/postgresql/data/pg_hba.conf