#!/bin/bash

docker exec -it kafka-schema-registry kafka-avro-console-consumer --bootstrap-server broker-1:19092 --topic $1 --from-beginning --property print.key=true;
