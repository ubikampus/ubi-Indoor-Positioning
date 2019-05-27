#!/bin/bash
set -ev
file="$TRAVIS_BUILD_DIR/config.properties"
echo "mqttUrl=mqtt" > $file
echo "subscribeTopic=ohtu/test/observations" >> $file
echo "publishTopic=ohtu/test/locations" >> $file
cat $file