#!/usr/bin/env sh

curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" \
http://localhost:8083/connectors/ -d @register.json