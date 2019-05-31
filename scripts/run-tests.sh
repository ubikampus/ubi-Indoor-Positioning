#!/bin/bash
set -ev

if [[ -z "$TRAVIS_BUILD_DIR" ]]; then
  file="config.properties"
else
  file="$TRAVIS_BUILD_DIR/config.properties"
fi

echo "mqttUrl=mqtt" > $file
echo "subscribeTopic=ohtu/test/observations" >> $file
echo "publishTopic=ohtu/test/locations" >> $file

echo "# rasps in format of x:y:z" >> $file

echo "rasp1=0:0:0" >> $file
echo "rasp2=33:50:66" >> $file
echo "rasp3=66:50:33" >> $file
echo "rasp4=100:100:100" >> $file
cat $file