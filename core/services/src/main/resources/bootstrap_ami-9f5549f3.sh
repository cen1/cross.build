#!/bin/sh
# FreeBSD 10
#install packages
pkg install -y sudo
pkg install -y bash
pkg install -y openjdk8-jre
pkg install -y unzip
pkg install -y screen
pkg install -y ezjail
pkg install -y git
#setup sudo and bash
ln /usr/local/bin/bash /bin/bash
echo "ec2-user ALL=(ALL) NOPASSWD:ALL" >> /usr/local/etc/sudoers