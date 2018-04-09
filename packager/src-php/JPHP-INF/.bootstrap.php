<?php

use packager\cli\ConsoleApp;
use packager\Plugin;
use php\lib\fs;
use php\lib\str;

global $app;

$app = new ConsoleApp();
$app->main($argv);