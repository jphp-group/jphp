<?php

use php\webserver\WebRequest;
use php\webserver\WebServer;

$server = new WebServer();

$server->setIsolated(true);
$server->setHotReload(true);

$server->setRoute(function (WebRequest $request) {
    require "src/Bootstrap.php";

    Bootstrap::run($request);
});

$server->addStaticHandler([
    'path' => '/assets/**',
    'location' => 'classpath:/assets/'
]);

$server->run();