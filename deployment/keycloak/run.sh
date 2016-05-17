#!/bin/bash
docker run -d --name crossbuild-keycloak --link crossbuild-postgres:postgres -p 8443:8443 -e POSTGRES_DATABASE=keycloak -e POSTGRES_USER=keycloak -e POSTGRES_PASSWORD=crossbuild2016ae -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=crossbuild2016ae crossbuild-keycloak
