#!/bin/bash

#JAR=target/infinstor-bitcoin-tx-reader-1.0-SNAPSHOT.jar

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

CP=""
for i in `find ${DIR} -name '*.jar'`
do
  CP=${CP}:$i
done

(cd $DIR; java -cp "$CP" com.infinstor.bitcoin.txreader.BitcoinTxReader $*)
