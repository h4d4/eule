#! /bin/bash

JIFC="jifc";
JIF="jif";

mkdir -p sig-classes;
${JIFC} -d sig-classes -sourcepath sig-src sig-src/java/util/*.jif;

mkdir -p jif-classes;
${JIFC} -d jif-classes -sourcepath jif-src jif-src/jif/principals/*.jif;
