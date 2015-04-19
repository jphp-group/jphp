<?php

use php\sql\SqlDriverManager;
use php\webserver\WebServer;

SqlDriverManager::install('sqlite');

$server = new WebServer(function() {
    $conn = SqlDriverManager::getConnection('sqlite::memory:');
    $conn->query('create table users (id int, name string)')->update();
    var_dump($conn->query('select COUNT(*) from users')->fetch()->get('COUNT(*)'));

    echo rand(10, 1000);
});

$server->setPort(8080);

$server->addStaticHandler([
    'path' => '/assets/**',
    'location' => 'classpath:/assets/',
    'gzip' => true,
    'cache' => true,
    'cachePeriod' => 3600,
]);

$server->run();