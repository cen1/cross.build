#!/bin/bash
# UBUNTU 14.04
#run loadmonitor
unzip loadmonitor.zip
chmod +x startmonitor.sh
mv startmonitor.sh ./loadmonitor
cd loadmonitor && screen -dmS loadmonitor ./startmonitor.sh

#loadmonitor restart
echo "@reboot cd /home/ubuntu/loadmonitor/ && /usr/bin/screen -dmS loadmonitor ./startmonitor.sh" >> loadcron
crontab loadcron