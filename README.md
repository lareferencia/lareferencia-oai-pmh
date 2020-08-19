#LA Referencia OAI-PMH Provider

##Install

1. Clone/download this repository

2. Build using Maven/Java8

```
$ ./build.sh
```

3. Edit config files

- config/application.properties

- config/xoai.config

- oai-pmh.conf

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


