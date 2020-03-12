#!/bin/bash

JAR=target/infinstor-bitcoin-tx-reader-1.0-SNAPSHOT.jar

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

CP=`mvn -f $DIR/../../.. dependency:build-classpath|grep -v INFO | grep -v WARN`

(cd $DIR/../../..; java -cp "$CP":$JAR com.infinstor.bitcoin.txreader.BitcoinTxReader $*)
