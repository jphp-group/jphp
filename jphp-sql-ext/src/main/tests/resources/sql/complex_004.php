--TEST--
SqlDriverManager test blobs #2
--FILE--
<?php
use php\sql\SqlDriverManager;

SqlDriverManager::install('sqlite');

$conn = SqlDriverManager::getConnection('sqlite::memory:');

$conn->query('create table person (id integer, file blob)')->update();

$count = $conn->query('insert into person values(?, ?)', [1, 'foobar'])->update();

var_dump($count);

var_dump($conn->query('select * from person')->fetch()->toArray());

?>
--EXPECT--
int(1)
array(2) {
  ["id"]=>
  int(1)
  ["file"]=>
  string(6) "foobar"
}
