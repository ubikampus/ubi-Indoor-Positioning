# Ubi Indoor Positioning

[![Build Status](https://travis-ci.org/ubikampus/ubi-Indoor-Positioning.svg?branch=master)](https://travis-ci.org/ubikampus/ubi-Indoor-Positioning)
[![codecov](https://codecov.io/gh/ubikampus/ubi-Indoor-Positioning/branch/master/graph/badge.svg)](https://codecov.io/gh/ubikampus/ubi-Indoor-Positioning)
[![](https://jitpack.io/v/ubikampus/ubi-Indoor-Positioning.svg)](https://jitpack.io/#ubikampus/ubi-Indoor-Positioning)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

Ubi Indoor Positioning is a java library that can be used to perform trilateration in n-dimensional space and to listen/publish messages in MQTT bus. Main functionality rotates around finding position of device with detected signal strength. However there is still build in integration for subscribing to MQTT bus topics and to publish into them.

## Table of Contents <a name="table-of-contents"/>

*  [Documentation](#documentation)
*  [Where can I get the latest release?](#where-can-i-get-the-latest-release)
*  [License](#license)
*  [Contributors](#contributors)
*  [Generally about the project](#generally-about-the-project)

## Documentation

<!-- replace javadoc link with this when next release out: https://javadoc.jitpack.io/com/github/ubikampus/ubi-Indoor-Positioning/latest/javadoc/ -->
* [API Javadoc](https://javadoc.jitpack.io/com/github/ubikampus/ubi-Indoor-Positioning/feature~README-1767b69a64-1/javadoc/): Documentation for the current release.
* [Changelog](https://github.com/ubikampus/ubi-Indoor-Positioning/blob/master/CHANGELOG.md): Changes in the recent versions.

## Where can I get the latest release?

Gradle

```
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.ubikampus:ubi-Indoor-Positioning:v0.1.0'
}
```

Maven

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
    </repository>
</repositories>
	
<dependency>
    <groupId>com.github.ubikampus</groupId>
    <artifactId>ubi-Indoor-Positioning</artifactId>
    <version>v0.1.0</version>
</dependency>
```

Alternatively source code is also available in [GitHub releases](https://github.com/ubikampus/ubi-Indoor-Positioning/releases).

Project uses [semantic versioning](https://semver.org/) in defining releases.

## License

Code is under the [MIT License](https://github.com/ubikampus/ubi-Indoor-Positioning/blob/master/LICENSE)

## Contributors

* [Aleksander Matikainen](https://github.com/alemati)
* [Atte Haarni](https://github.com/Ajhaa)
* [Elizabeth Berg](https://github.com/reykjaviks)
* [Emil Andersson](https://github.com/andeem)
* [Jere Lahelma](https://github.com/je-l)
* [Joni Kokko](https://github.com/Jhoneagle)
* [Matti Riekkinen](https://github.com/mriekkin)

## Generally about the project

This trilateration library is part of larger project to create comprehensive implementation of indoor positioning system for ubikampus. 
The system consist of [server](https://github.com/ubikampus/server_program) which uses this library to calculate location of BLE devices using rssi collected by Rasperries, 
[visualizer](https://github.com/ubikampus/bluetooth-dev-visualizer) for using location data gotten from server, 
[Rasperry scanner](https://github.com/ubikampus/bluetooth-raspberry-scanner) to scan for BLE devices and to collect their signal strength and 
[Rasperry control tool](https://github.com/ubikampus/raspberry-config-cli) to scale grid which Rasperries form.

### Project related links

* [Hours](https://docs.google.com/spreadsheets/d/1pgeD1oTm5cBeNS73Hs8ie4iGBln6UkSQHe_rVjXCOTo/edit?usp=sharing)
* [Backlog](https://docs.google.com/spreadsheets/d/1dOJzTgOaNfIl2t6UdaIPswVt-OaKmjpm_03oe0t5R6o/edit?usp=sharing)


