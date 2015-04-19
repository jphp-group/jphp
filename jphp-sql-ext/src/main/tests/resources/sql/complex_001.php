--TEST--
Test create table #1
--FILE--
<?php
use php\sql\SqlDriverManager;
SqlDriverManager::install('sqlite');

$conn = SqlDriverManager::getConnection('sqlite::memory:');

$statement = $conn->query('create table person (id integer, name string)');
var_dump($statement->update());

$result = $conn->query('select COUNT(*) from person')->fetch();
var_dump($result->toArray());

?>
--EXPECTF--
int(0)
array(1) {
  ["COUNT(*)"]=>
  int(0)
}