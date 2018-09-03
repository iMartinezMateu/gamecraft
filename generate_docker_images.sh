#!/bin/sh

echo "Generating docker images"
cd ./gamecraft-email-notification-manager
./mvnw verify -Pprod dockerfile:build
cd ../gamecraft-engine-manager
./mvnw verify -Pprod dockerfile:build
cd ../gamecraft-gateway
./mvnw verify -Pprod dockerfile:build -DskipTests
cd ../gamecraft-irc-notification-manager
./mvnw verify -Pprod dockerfile:build
cd ../gamecraft-pipeline-manager
./mvnw verify -Pprod dockerfile:build
cd ../gamecraft-project
./mvnw verify -Pprod dockerfile:build
cd ../gamecraft-slack-notification-manager
./mvnw verify -Pprod dockerfile:build
cd ../gamecraft-telegram-notification-manager
./mvnw verify -Pprod dockerfile:build
cd ../gamecraft-twitter-notification-manager
./mvnw verify -Pprod dockerfile:build
cd ../gamecraft-ui
./mvnw verify dockerfile:build
cd ../

echo "Generation already done. Now issue docker compose command."



