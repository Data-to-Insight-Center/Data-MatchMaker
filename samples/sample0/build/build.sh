#!/bin/sh
# This script first generates pojo classes for the json profiles used by the current
# sample. Then it brings original matchmaker code together with generated classes and
# compiles the full source code into a jar file mm.jar and copies it into the lib
# directory so that it's added to the classpath when the sample is run.

ROOT=$(pwd)
CLASSES=$(pwd)
SRC=$ROOT/src
TMP=$ROOT/temp
CMP=$ROOT/comp
RESOURCES=$ROOT/../
LIB=../../lib

echo
echo "#########################################"
echo "#               build.sh                #"
echo "#########################################"
echo
echo "Generating Matchmaker jar with generated pojo classes"
echo

for i in $(ls $LIB |grep ".jar"); do
        CLASSES=$CLASSES:$LIB/$i
done

rm -rf $SRC $TMP $CMP
mkdir $SRC $TMP $CMP

CP=:$CLASSPATH:$CLASSES:.
javac -classpath $CP -d $ROOT ../../../src/main/java/edu/indiana/d2i/matchmaker/core/POJOFactory.java
java -classpath $CP edu.indiana.d2i.matchmaker.core.POJOFactory $SRC $RESOURCES

rm -rf edu

cp -r ../../../src/main/java/* $TMP
rm -rf $TMP/edu/indiana/d2i/matchmaker/pojo
cp -r $SRC/* $TMP

find $TMP -name "*.java" | xargs javac -classpath $CP -d $CMP
jar cvf mm.jar -C $CMP .
cp mm.jar $LIB

rm -rf $CMP $SRC $TMP

echo
echo "#########################################"
echo "#     Successfully Generated mm.jar     #"
echo "#########################################"
echo
