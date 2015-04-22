<?php

use php\io\Stream;
use php\lib\String;
use php\webserver\WebRequest;
use php\webserver\WebServer;

echo String::random();

/*$server = new WebServer();

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

$server->run();  */
