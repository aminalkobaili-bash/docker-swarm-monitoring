#!/bin/bash
sudo docker swarm init --advertise-addr 192.168.56.10
sudo docker swarm join-token worker -q > /vagrant/worker_token