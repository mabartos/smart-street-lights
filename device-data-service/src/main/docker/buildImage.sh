#!/bin/bash
# Build docker image
cd ../../../ && ./mvnw clean package -Dquarkus.package.type=fast-jar -DskipTests && \
docker build -f src/main/docker/Dockerfile.fast-jar -t quarkus/data-service-fast-jar .
