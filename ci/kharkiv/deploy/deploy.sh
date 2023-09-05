#!/bin/bash
echo "DELETING previous docker-compose..."
sudo docker-compose down

echo "-------------------------------------------------------------------------"
echo "CREATING folder for github project..."
sudo mkdir source
cd source

echo "-------------------------------------------------------------------------"
echo "DELETING previous project folder..."
sudo rm -rf KharkivBot

echo "-------------------------------------------------------------------------"
echo "CLONING KharkivBot..."
sudo git clone https://2c0bcb4ba96d4bca05626a5d3ca17570ae896bd9@github.com/DaniilMuntyan/KharkivBot.git

echo "-------------------------------------------------------------------------"
echo "COMPILATION..."
cd KharkivBot
sudo mvn clean package -Dmaven.test.skip=true

echo "-------------------------------------------------------------------------"
echo "DOCKER image building..."
sudo docker build --build-arg=target/demo-0.0.1.jar -t kharkiv_bot:v1 .
cd ../
cd ../

echo "-------------------------------------------------------------------------"
echo "STARTING java spring app..."
sudo docker-compose -f docker-compose.yml up -d app

#echo "WAITING until postgres_data volume was created..."
#sleep 4

#echo "-------------------------------------------------------------------------"
#echo "CHOWN volumes to current user..."
#sudo chown -R $USER:$USER ./volumes
#ls -l ./volumes
