![Build](https://github.com/lareferencia/lareferencia-oai-pmh/workflows/Build/badge.svg)


# Redalyc OAI-PMH Provider

## Install

1. Clone/download this repository

2. Build using Maven/Java8

```
$ ./build.sh
```

3. Edit config files

> - config/application.properties

> - config/xoai.config

> - oai-pmh.conf

## Configure Solr Core

1. Copy oai core ( solr.core/oai ) to a compatible Solr installation ( tested with 6.6 )

2. Restart Solr server

*Note: a Docker solr container with preconfigured oai core is provided 

## Run

- Run from bash

```
$ ./oai-pmh.jar
```

- Run as service

```
$ sudo ln -s /path/to/oai-pmh.jar /etc/init.d/oai-pmh
$ sudo /etc/init.d/oai-pmh start
``` 

- Navigate to http://host:port/ 


