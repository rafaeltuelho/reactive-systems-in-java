#!/usr/bin/env sh

docker-compose exec kafka /kafka/bin/kafka-console-consumer.sh \
--bootstrap-server kafka:9092 \
--from-beginning \
--property print.key=true \
--topic quarkus-db-server.public.customer | jq .
