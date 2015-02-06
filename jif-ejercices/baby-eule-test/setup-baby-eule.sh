#! /bin/bash

JIFC="jifc";
JIF="jif";



mkdir -p jif-classes;
${JIFC} -d jif-classes -sourcepath jif-src jif-src/jif/principals/*.jif;


####
#This script compiles with jif compiler the principals requires for your jif project, that you put in the jif-src/jif/principals/ directory
#Eg Alice.jif, Bob.jif

##jif arguments  
#-d output directory (default: current directory)
#-sourcepath <path>       where to find source files (default: current directory)
#                           

