# Bluetooth-location-server

[![Build Status](https://travis-ci.org/ubikampus/Bluetooth-location-server.svg?branch=master)](https://travis-ci.org/ubikampus/Bluetooth-location-server)
[![codecov](https://codecov.io/gh/ubikampus/Bluetooth-location-server/branch/master/graph/badge.svg)](https://codecov.io/gh/ubikampus/Bluetooth-location-server)

See ohtu-project specific documentation in /ohtudoc directory.

Short description TODO.

### Local development

#### Docker

Easiest way to get a development server running is to use docker-compose.
Create a config.properties -file, and define `mqttUrl=mqtt`, `subscribeTopic=ohtu/test/observations` and `publishTopic=ohtu/test/locations`.  
Set up the development environment with `docker-compose up`.
You should now have both the location server and an mqtt server running.
The mqtt server has port 1883 exposed, so you can also connect to it from the outside.

src and config.properties are shared to the container, so you don't have to rebuild the image when making changes. Just restart the server.

If you just need the location server, set `mqttUrl` to the mqtt server's url, and run `docker-compose up btls`.

### Installation
TODO

### Usage
TODO

### Contributing

Jhoneagle - Joni Kokko

Ajhaa - Atte Haarni 

alemati - Aleksander Matikainen 

andeem - Emil Andersson

je-l  - Jere Lahelma

mriekkin - Matti Riekkinen

### Links

[Visualizer for development](https://github.com/ubikampus/bluetooth-dev-visualizer)

[Backlog](https://docs.google.com/spreadsheets/d/1ypNhF0JG-SiwpOhV2lv1u9FnfyDAChQ84imtakCc800/edit#gid=7)



