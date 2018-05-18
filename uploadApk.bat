@echo off
SET SrcPath=.\app\build\outputs\apk\debug
SET TargetDir=d:\soft\Dropbox\Projects\ObdNotifier\output

XCOPY /Y %SrcPath%\*.apk %TargetDir%\obdNotifier.*