#!/usr/bin/env bash
cd "$(dirname "$0")"
java -cp core-service/target/core-service-1.0-SNAPSHOT.jar com.bank.client.ConsoleClientApplication
