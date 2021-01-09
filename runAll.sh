docker-compose -f user-service/src/main/docker/docker-compose.yaml --env-file=user-service/src/main/docker/.env up && \
docker-compose -f device-service/src/main/docker/docker-compose.yaml --env-file=device-service/src/main/docker/.env up
