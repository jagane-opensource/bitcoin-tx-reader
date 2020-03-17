#!/bin/bash
CP=`mvn dependency:build-classpath|grep -v INFO | grep -v WARN`
CPS=$(echo ${CP} | tr ":" "\n")

/bin/rm -rf /tmp/tdir.$$
/bin/mkdir /tmp/tdir.$$

for i in ${CPS}
do
  echo $i
  /bin/cp -f $i /tmp/tdir.$$
done
/bin/cp -f target/infinstor-bitcoin-tx-reader-*.jar /tmp/tdir.$$
/bin/cp -f src/main/scripts/run-reader.sh /tmp/tdir.$$

tar czf target/package.tgz -C /tmp/tdir.$$ .
/bin/rm -rf /tmp/tdir.$$
