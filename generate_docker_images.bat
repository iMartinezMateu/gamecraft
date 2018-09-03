echo Generating docker images
cd %~dp0\gamecraft-email-notification-manager
mvnw verify -Pprod dockerfile:build
cd %~dp0\gamecraft-engine-manager
mvnw verify -Pprod dockerfile:build
cd %~dp0\gamecraft-gateway
mvnw verify -Pprod dockerfile:build -DskipTests
cd %~dp0\gamecraft-irc-notification-manager
mvnw verify -Pprod dockerfile:build
cd %~dp0\gamecraft-pipeline-manager
mvnw verify -Pprod dockerfile:build
cd %~dp0\gamecraft-project
mvnw verify -Pprod dockerfile:build
cd %~dp0\gamecraft-slack-notification-manager
mvnw verify -Pprod dockerfile:build
cd %~dp0\gamecraft-telegram-notification-manager
mvnw verify -Pprod dockerfile:build
cd %~dp0\gamecraft-twitter-notification-manager
mvnw verify -Pprod dockerfile:build
cd %~dp0\gamecraft-ui
mvnw verify -Pprod dockerfile:build
cd %~dp0\

echo Generation already done. Now issue docker compose command.



