sudo sysctl net.ipv4.ip_forward=1
sudo iptables -t nat -A POSTROUTING -j MASQUERADE
sudo iptables -A INPUT -j LOG

#allow all requests for CA manager
sudo iptables -t nat -A PREROUTING -p TCP --dport 8443 -j DNAT --to-destination 192.168.20.160:8443


#sudo java -jar AuthenticationGW-0.0.1-SNAPSHOT.jar
#sudo java -jar AccessGW-0.0.1-SNAPSHOT.jar 
