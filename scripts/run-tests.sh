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
cat $file