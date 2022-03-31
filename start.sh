#!/bin/bash

./mvnw -D style.color=always clean package
./mvnw -D style.color=always verify -P with-integration-tests