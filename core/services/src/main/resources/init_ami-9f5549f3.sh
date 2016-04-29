#!/bin/bash
# FREEBSD 10
#prepare ezjail and networking
export PATH=/usr/local/bin:$PATH:.
hash -r
ezjail-admin install > /dev/null
sysrc -f /etc/rc.conf ezjail_enable="YES"
sysrc -f /etc/rc.conf cloned_interfaces="lo1"
service netif cloneup
kldload pf
sysrc -f /etc/rc.conf pf_enable="YES"
IP_PUB=$(ifconfig xn0 | grep 'inet' | cut -d ' ' -f 2)
echo 'IP_PUB="'$IP_PUB'"' >> /etc/pf.conf
echo 'NET_JAIL="192.168.0.0/16"' >> /etc/pf.conf
echo "scrub in all" >> /etc/pf.conf
echo 'nat pass on xn0 from $NET_JAIL to any -> $IP_PUB' >> /etc/pf.conf
pfctl -f /etc/pf.conf
service pf start
cp -R /usr/jails/flavours/example /usr/jails/flavours/base
cp /etc/resolv.conf /usr/jails/flavours/base/etc/
cp ezjail.flavour /usr/jails/flavours/base