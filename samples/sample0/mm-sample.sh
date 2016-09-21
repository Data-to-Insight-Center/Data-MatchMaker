
#!/bin/sh
LIB=../lib

for i in $(ls $LIB |grep ".jar"); do
        CLASSES=$CLASSES:$LIB/$i
done

echo

CP=:$CLASSPATH:$CLASSES:.
java -classpath $CP edu.indiana.d2i.matchmaker.SampleExecutor config/matchmaker.properties $1

