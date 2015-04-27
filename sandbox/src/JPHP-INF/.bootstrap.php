<?php

use php\lang\Environment;
use php\webserver\WebRequest;
use php\webserver\WebResponse;
use php\webserver\WebServer;

$server = new WebServer();

$server->setEnvironmentCreator(function () {
    $env = new Environment(null, Environment::HOT_RELOAD);
    $env->importAutoLoaders();
    return $env;
});

$server->setRoute(function (WebRequest $request, WebResponse $response) {
    Bootstrap::run($request, $response);
});

$server->run();