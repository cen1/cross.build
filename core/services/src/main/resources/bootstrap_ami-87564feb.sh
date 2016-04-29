#!/bin/bash
# UBUNTU 14.04
#install packages
add-apt-repository -y ppa:openjdk-r/ppa
apt-get update
apt-get -y install openjdk-8-jdk
apt-get -y install unzip
apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
truncate --size=0 /etc/apt/sources.list.d/docker.list
echo 'deb https://apt.dockerproject.org/repo ubuntu-trusty main' | sudo tee --append /etc/apt/sources.list.d/docker.list
apt-get -y update
apt-get -y install docker-engine
service docker start