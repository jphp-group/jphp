<?php

use packager\cli\ConsoleApp;

global $app;

$app = new ConsoleApp();
$app->main($argv);