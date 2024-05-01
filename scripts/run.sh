#!/bin/bash

cd ..

java \
"-agentlib:jdwp=transport=dt_socket,server=n,address=host.docker.internal:5005,suspend=y" \
"-Dlogging.level.io.sealights.tool=TRACE" \
-jar ./build/libs/line-coverage-tool-1.0-SNAPSHOT-standalone.jar \
--token asd \
--startCommit 813c756e76df7593aa10f2b3b33b1eb4fcdbf89d

