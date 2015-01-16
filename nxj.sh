#!/bin/sh

export LEJOS_HOME=~/git/3p/leJOS_NXJ_0.9.1beta-3
export LEJOS_BIN=$LEJOS_HOME/bin
export LEJOS_CLASSES=$LEJOS_HOME/lib/nxt/classes.jar

if [ "$1" = "build" ] ; then
  $LEJOS_BIN/nxjc -cp $LEJOS_CLASSES:target/classes -d target/classes -sourcepath src/main/nxt src/main/nxt/nxtclj/*.java
  $LEJOS_BIN/nxjlink -bp $LEJOS_CLASSES -cp target/classes -o target/nxtclj.nxj nxtclj.Interpreter
elif [ "$1" = "upload" ] ; then
  $LEJOS_BIN/nxjupload -u target/nxtclj.nxj
else
  echo "Usage: $0 [build | upload]"
fi

