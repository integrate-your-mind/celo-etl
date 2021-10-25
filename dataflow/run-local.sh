#!/usr/bin/env bash


mvn -Pdirect-runner compile exec:java \
-Dexec.mainClass=io.blockchainetl.celo.CeloPubSubToBigQueryPipeline \
-Dexec.args="--chainConfigFile=chainConfig.json --allowedTimestampSkewSeconds=36000 --defaultSdkHarnessLogLevel=DEBUG \
--tempLocation=gs://<project_name>/dataflow"
