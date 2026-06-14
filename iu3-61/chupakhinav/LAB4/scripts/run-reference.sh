#!/usr/bin/env bash
set -euo pipefail
mvn compile exec:java -Dexec.mainClass=ru.bmstu.travel.reference.ReferenceServer
