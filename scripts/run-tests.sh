#!/bin/bash
set -ev
file="../app/config.properties"
echo "mqttUrl=mqtt" > $file
echo "subscribeTopic=ohtu/test/observations" >> $file
echo "publishTopic=ohtu/test/locations" >> $file
cat $file