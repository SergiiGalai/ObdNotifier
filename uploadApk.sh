#!/bin/bash
SrcPath=./app/build/outputs/apk/free/debug
TargetDir=/home/cheb/Dropbox/Projects/ObdNotifier/output

echo $TargetDir
yes | cp -rvf $SrcPath/*.apk $TargetDir/carNotifier.*
