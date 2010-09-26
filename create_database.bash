#!/bin/bash
#$1 is user
if [ -n "$1" ]; then
    echo create database sr | mysql -u "$1" -p
else
    echo "error, supply username!"
fi
