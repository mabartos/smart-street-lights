#!/bin/bash

# Workaround for running all services
# Rather execute individually

declare -a serviceList

# TEMP: Add name of services here
serviceList=("user-service" "device-service" "device-data-service" "simulation-data-service" "city-service" "street-service")
homeDir=$(pwd);

for service in "${serviceList[@]}"; do
  echo "Executing the ${service}."
  cd "${homeDir}"/"${service}"/src/main/docker && docker-compose up -d &
done

