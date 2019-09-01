#!/bin/bash
rm -rf target
sbt "test:compile"
DATA_SCALA=`sbt "testOnly KmeanTest"`
DATA_JAVA=`sbt "testOnly javaTest"`
python parseResults.py "$DATA_SCALA" "$DATA_JAVA"