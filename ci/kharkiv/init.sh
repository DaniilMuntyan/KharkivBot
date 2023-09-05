#!/bin/bash
sudo apt-get update
sudo apt-get upgrade

echo "--------------------------------------------------------------------------------------------"
echo "INSTALLING docker..."
sudo apt-get install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu  $(lsb_release -cs)  stable"
sudo apt-get update
sudo apt-cache madison docker-ce
sudo apt-get install docker-ce

echo "--------------------------------------------------------------------------------------------"
echo "MAKING docker sudoer..."
sudo groupadd docker
sudo usermod -aG docker "${USER}"

echo "--------------------------------------------------------------------------------------------"
echo "INSTALLING docker-compose..."
sudo curl -L https://github.com/docker/compose/releases/download/1.28.6/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
echo "PERMISSIONS for docker-compose..."
sudo chmod +x /usr/local/bin/docker-compose

echo "--------------------------------------------------------------------------------------------"
echo "INSTALLING maven..."
sudo apt install maven

echo "--------------------------------------------------------------------------------------------"
echo "INSTALLING git..."
sudo apt install git

echo "--------------------------------------------------------------------------------------------"
echo "GIT version"
sudo git --version

echo "--------------------------------------------------------------------------------------------"
echo "PROVIDING my name and email address to git"
sudo git config --global user.name "Danyil"
sudo git config --global user.email "daniilmuntjan@gmail.com"
sudo git config --list

echo "--------------------------------------------------------------------------------------------"
echo "RUNNING pgadmin docker"
sudo docker run -d -p 5050:80 --name pgadmin \
		--env PGADMIN_DEFAULT_EMAIL=daniilmuntjan@gmail.com \
		--env PGADMIN_DEFAULT_PASSWORD=admin2001 \
		dpage/pgadmin4

echo "--------------------------------------------------------------------------------------------"
echo "CREATING deploy folder"
sudo mkdir deploy

echo "--------------------------------------------------------------------------------------------"
echo "CREATING volumes folder"
sudo mkdir ./deploy/volumes

echo "--------------------------------------------------------------------------------------------"
echo "CREATING source folder"
sudo mkdir ./deploy/source
