# Bluetooth Location Server

[![Build Status](https://travis-ci.org/ubikampus/Bluetooth-location-server.svg?branch=master)](https://travis-ci.org/ubikampus/Bluetooth-location-server)
[![codecov](https://codecov.io/gh/ubikampus/Bluetooth-location-server/branch/master/graph/badge.svg)](https://codecov.io/gh/ubikampus/Bluetooth-location-server)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

<!-- TODO: Description -->

## Table of Contents <a name="table-of-contents"/>
*  [Local Development](#local-development)
*  [Installation](#installation)
*  [Usage](#usage)
*  [Contributors](#contributors)
*  [Documentation](#documentation)

## Local Development
### Docker

Easiest way to get a development server running is to use docker-compose.
Create a config.properties -file, and define `mqttUrl=mqtt`, `subscribeTopic=ohtu/test/observations` and `publishTopic=ohtu/test/locations`.  
Set up the development environment with `docker-compose up`.
You should now have both the location server and an mqtt server running.
The mqtt server has port 1883 exposed, so you can also connect to it from the outside.

src and config.properties are shared to the container, so you don't have to rebuild the image when making changes. Just restart the server.

If you just need the location server, set `mqttUrl` to the mqtt server's url, and run `docker-compose up btls`.

### Installation

## Usage

## Contributors

* [Atte Haarni](https://github.com/Ajhaa)
* [Aleksander Matikainen](https://github.com/alemati)
* [Emil Andersson](https://github.com/andeem)
* [Elizabeth Berg](https://github.com/reykjaviks)
* [Joni Kokko](https://github.com/Jhoneagle)
* [Jere Lahelma](https://github.com/je-l)
* [Matti Riekkinen](https://github.com/mriekkin)

## Documentation

* [Hours](https://docs.google.com/spreadsheets/d/1pgeD1oTm5cBeNS73Hs8ie4iGBln6UkSQHe_rVjXCOTo/edit?usp=sharing)
* [Backlog](https://docs.google.com/spreadsheets/d/1ypNhF0JG-SiwpOhV2lv1u9FnfyDAChQ84imtakCc800/edit#gid=7)
* [Visualizer For Development](https://github.com/ubikampus/bluetooth-dev-visualizer)
