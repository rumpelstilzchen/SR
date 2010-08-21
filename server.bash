#!/bin/bash
#$1 = server hostname/ip to bind to
java -Dakka.config=config/akka.conf -jar target/SR-1.0-SNAPSHOT-jar-with-dependencies.jar --server $1
