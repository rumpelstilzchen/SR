#!/bin/bash
#$1=server ip or hostname
java -Dakka.config=config/akka.conf -jar target/SR-1.0-SNAPSHOT-jar-with-dependencies.jar --haltest $1
