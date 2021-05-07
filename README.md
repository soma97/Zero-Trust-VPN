# Podešavanje okruženja - 3 unaprijed pripremljene virtuelne mašine (aplikacije instalirane i podešene)

## Ciljani servisi

Na ciljanim servisima pokrenuti Docker container-e (sudo docker start {container_name}):

MySQL (3306)				// otvaranje MySQL servisa - ujedno ga koristi i AccessController

CA_Manager (8443)			// otvaranje servisa za izdavanje, praćenje i povlačenje digitalnih sertifikata koji je javno dostupan svim klijentima

U direktorijumu http-content pokrenuti komandu:
sudo python3 -m http.server 80		// otvaranje HTTP (80) index servisa

## AuthGW i AccessGW

Na AuthGW i AccessGW-u podesiti /etc/hosts za:
access.ctr	// adresa AccessController-a

Provjeriti da li su podešavanja IP tabela prazna koristeći komande:
sudo iptables -t nat -L
sudo iptables -L

Pokrenuti sudo bash startup.sh sa izmjenjenom IP adresom --to-destination {access.ctr adresa}:8443

Pokrenuti AuthGW i AccessGW koristeći komandu:
sudo java -jar {ime_app.jar}

## AccessController

Na Access Controller-u uraditi samo prvi put:
Na OS instalirati root digitalni sertifikat CA tijela (kojeg koristi CA_Manager) kako bi svi sertifikati koje je on potpisao postali Trusted (kopirati CA sertifikat, te ažurirati listu svih CA sertifikata)

Na Access Controller-u podesiti /etc/hosts za:
auth.gw		// adresa AuthGW-a
access.gw	// adresa AccessGW-a
my.ip		// sopstvena privatna IP adresa
dest.srv	// IP adresa ciljnih servisa kojima sistem raspolaže

Provjeriti da li su podešavanja IP tabela prazna koristeći komande:
sudo iptables -t nat -L
sudo iptables -L

Pokrenuti sudo bash startup.sh sa izmjenjenom IP adresom --to-destination {dest.srv adresa}:8443

Pokrenuti AccessController koristeći komandu:
sudo java -jar AccessController.jar
