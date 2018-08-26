#!/usr/bin/env bash

source ././../../sonar/token
cd ../
mvn sonar:sonar   -Dsonar.organization=shma2001gmailcom-github \
        -Dsonar.host.url=https://sonarcloud.io \
        -Dsonar.login=${token}
chromium-browser https://sonarcloud.io/project/issues?id=org.misha%3Amap-tree
