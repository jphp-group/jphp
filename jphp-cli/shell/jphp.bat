@echo off

set ROOT=%~dp0

java -Dfile.encoding=UTF8 -Xms20m -cp "%ROOT%/libs/*;" org.develnext.jphp.cli.CLI %*
