#!/bin/bash
docker build -t crossbuild-web .
docker run -p 80:3002 --name crossbuild-web crossbuild-web