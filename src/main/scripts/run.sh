#!/bin/bash
CP=`mvn dependency:build-classpath|grep -v INFO | grep -v WARN`

JAR=target/infinstor-bitcoin-tx-reader-1.0-SNAPSHOT.jar

java -cp "$CP":$JAR com.infinstor.bitcoin.txreader.BitcoinTxReader $*
