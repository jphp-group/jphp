<?php

use php\sql\SqlDriverManager;
use php\sql\SqlResult;

SqlDriverManager::install('sqlite');

$conn = SqlDriverManager::getConnection('sqlite::memory:');

$conn->query('create table person (id integer, name string)')->update();
$conn->query("insert into person values(?, ?)", [1, 'leo'])->update();
$conn->query("insert into person values(?, ?)", [2, 'yui'])->update();

/** @var SqlResult $row */
foreach ($conn->query('select * from person') as $row) {
    var_dump($row->toArray());
}