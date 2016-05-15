#!/bin/bash
docker run -d --name crossbuild-jenkins -p 8081:8081 -p 50000:50000 jenkins:2.0 --httpPort=8081