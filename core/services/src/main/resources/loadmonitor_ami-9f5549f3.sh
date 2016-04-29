#!/bin/bash
# FREEBSD 10
#run loadmonitor
export PATH=/usr/local/bin:$PATH:.
hash -r
unzip loadmonitor.zip
chmod +x startmonitor.sh
mv startmonitor.sh ./loadmonitor
cd loadmonitor
echo $PATH
screen -dmS loadmonitor ./startmonitor.sh

#loadmonitor restart
echo "@reboot cd /home/ec2-user/loadmonitor/ && /usr/local/bin/screen -dmS loadmonitor ./startmonitor.sh" >> loadcron
crontab loadcron