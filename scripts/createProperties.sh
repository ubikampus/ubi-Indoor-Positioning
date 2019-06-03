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
cat $mqtt

echo "# rasps locations in format of x:y:z" >> $rasp
echo "rasp-1=8000:3600:0" >> $rasp
echo "rasp-2=0:0:66" >> $rasp
echo "rasp-3=66:50:33" >> $rasp
echo "rasp-4=0:4000:100" >> $rasp
cat $rasp