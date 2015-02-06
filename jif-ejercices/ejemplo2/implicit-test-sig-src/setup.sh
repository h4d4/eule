#! /bin/bash

JIFC="jifc";
JIF="jif";

mkdir -p jif-classes;
${JIFC} -d jif-classes -sourcepath jif-src jif-src/jif/principals/*.jif;
#compiles with jif compiler the principals requires for your jif project, that you put in the jif-src/jif/principals/ directory Eg Alice.jif, Bob.jif
##jif arguments  
#-d output directory (default: current directory)
#-sourcepath <path>       where to find source files (default: current directory)
mkdir -p sig-classes;
${JIFC} -d sig-classes -sourcepath sig-src sig-src/java/util/*.jif;
#${JIFC} -d sig-classes -sourcepath sig-src sig-src/android/app/*.jif;
${JIFC} -d sig-classes -sourcepath sig-src sig-src/android/telephony/*.jif;
${JIFC} -d sig-classes -sourcepath sig-src sig-src/android/content/*.jif;
${JIFC} -d sig-classes -sourcepath sig-src sig-src/android/os/*.jif;
${JIFC} -d sig-classes -sourcepath sig-src sig-src/android/util/*.jif;
${JIFC} -d sig-classes -sourcepath sig-src sig-src/android/view/*.jif;
${JIFC} -d sig-classes -sourcepath sig-src sig-src/android/widget/*.jif;
#${JIFC} -d sig-classes -sourcepath sig-src sig-src/java/lang/*.jif;
#compiles the additional java classes with jif compiler; produce version jif version of java code

