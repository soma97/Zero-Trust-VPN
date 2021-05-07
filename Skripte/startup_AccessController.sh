sudo sysctl net.ipv4.ip_forward=1
sudo iptables -t nat -A POSTROUTING -j MASQUERADE

#allow all requests for CA manager
sudo iptables -t nat -A PREROUTING -p TCP --dport 8443 -j DNAT --to-destination 192.168.20.161:8443

sudo java -jar AccessController.jar
