FROM postgres:9.5

ENV POSTGRES_PASSWORD postgres
EXPOSE 5432
ADD extend.sh /docker-entrypoint-initdb.d/
ADD migrate.sql /tmp