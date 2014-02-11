@echo off

set ROOT=%~dp0

java -Xms20m -jar "%ROOT%/jphp-cli.jar" %*
