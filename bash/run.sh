#!/bin/sh
app='map-tree'
M2_HOME='/usr/share/maven'
export M2_HOME
mvn=${M2_HOME}/bin/mvn
cd ../
${mvn} clean install -X
if [ -e dist ]; then rm -r dist; fi
mkdir dist
cd target
cp ${app}.jar ../dist
cp -r lib ../dist
cp -r config ../dist
cd ../dist
java -jar ${app}.jar
