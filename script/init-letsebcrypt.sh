#!/bin/bash

domains=(hyunsolution.duckdns.org)
rsa_key_size=4096
data_path="./data/certbot"
email="nomadnest242@gmail.com"

if [ -d "$data_path" ]; then
  read -p "Existing data found for $domains. Continue and replace existing certificate? (y/N) " decision
  if [ "$decision" != "Y" ] && [ "$decision" != "y" ]; then
    exit
  fi
fi

mkdir -p "$data_path/conf/live/$domains"
mkdir -p "$data_path/www"

rm -rf ./data/nginx/*
touch ./data/nginx/app.conf