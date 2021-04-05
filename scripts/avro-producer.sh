#!/bin/bash

docker exec -i kafka-schema-registry kafka-avro-console-producer --broker-list broker-1:19092 --topic video-streams --property value.schema="$(< ./src/main/avro/video-event.avsc)" --property parse.key=true --property key.schema='{"type":"string"}' --property key.separator=" "  --property schema.registry.url=http://kafka-schema-registry:8081
