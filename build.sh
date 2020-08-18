#!/bin/bash
mvn clean package install -DskipTests -Dmaven.javadoc.skip=true
cp target/lareferencia-oai-pmh-2.0.1.jar oai-pmh.jar
