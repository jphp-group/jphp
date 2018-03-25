<?php

use packager\cli\ConsoleApp;
use packager\Plugin;

Plugin::registerLoaders('./plugins/');

$app = new ConsoleApp();
$app->main($argv);