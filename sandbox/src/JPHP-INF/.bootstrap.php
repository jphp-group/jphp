<?php

use php\io\Stream;
use php\lang\System;
use php\lib\String;
use php\webserver\WebRequest;
use php\webserver\WebResponse;
use php\webserver\WebServer;

$server = new WebServer();

$production = false;
$env = System::getEnv();

if ($env['PRODUCTION']) {
    $production = true;
}

$server->setIsolated(true);
$server->setHotReload(!$production);

$server->setRoute(function (WebRequest $request, WebResponse $response) use ($production) {
    if ($production) {
        require "res://Bootstrap.php";
    } else {
        require "src/Bootstrap.php";
    }

    Bootstrap::run($request, $response);
});

$server->addStaticHandler([
    'path' => '/assets/**',
    'location' => 'classpath:/assets/'
]);

$server->run();
