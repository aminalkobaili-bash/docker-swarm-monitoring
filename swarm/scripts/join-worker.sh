#!/bin/bash
# $1 - IP ноды в private_network (192.168.56.x)
NODE_IP=$1

# Ждем, пока менеджер создаст файл с токеном
while [ ! -f /vagrant/worker_token ]; do
  sleep 2
done
TOKEN=$(cat /vagrant/worker_token)

# Если нода уже была в swarm (например, с неправильным адресом) - выходим из него
sudo docker swarm leave --force 2>/dev/null

# Без --advertise-addr Docker берет адрес NAT-интерфейса 10.0.2.15,
# который одинаковый на всех VM, и overlay-сеть между нодами не работает
sudo docker swarm join --token $TOKEN --advertise-addr $NODE_IP 192.168.56.10:2377
