<?php

use packager\cli\ConsoleApp;
use packager\Plugin;
use php\lib\fs;
use php\lib\str;

$pluginDir = "./buildSrc";

spl_autoload_register(function ($className) use ($pluginDir) {
    $file = $pluginDir . '/' . str::replace($className, '\\', '/') . '.php';

    if (fs::isFile($file)) {
        require $file;
    }
});

$app = new ConsoleApp();
$app->main($argv);