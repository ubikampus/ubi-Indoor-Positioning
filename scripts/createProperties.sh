#!/bin/bash
set -ev

if [[ -z "$TRAVIS_BUILD_DIR" ]]; then
  mqtt="config/mqttConfig.properties"
  rasp="config/rasps.properties"
else
  mqtt="$TRAVIS_BUILD_DIR/mqttConfig.properties"
  rasp="$TRAVIS_BUILD_DIR/rasps.properties"
fi

echo "mqttUrl=mqtt" > $mqtt
echo "subscribeTopic=ohtu/test/observations" >> $mqtt
echo "publishTopic=ohtu/test/locations" >> $mqtt
echo "debug=true" >> $mqtt

echo "# rasps locations in format of x:y:z" > $rasp
echo "rasp-1=8000:3600:0" >> $rasp
echo "rasp-2=0:0:6666" >> $rasp
echo "rasp-3=6000:8000:3333" >> $rasp
echo "rasp-4=0:4000:10000" >> $rasp

cat $mqtt
cat $rasp