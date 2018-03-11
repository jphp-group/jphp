--TEST--
SqlDriverManager test transactions #2
--FILE--
<?php
use php\sql\SqlDriverManager;
use php\sql\SqlResult;
use php\util\Flow;

SqlDriverManager::install('sqlite');

$conn = SqlDriverManager::getConnection('sqlite::memory:');

$conn->autoCommit = false;

$conn->query('create table person (id integer)')->update();
$conn->commit();

$count = $conn->query('insert into person values(?)', [1])->update();

var_dump($count);

$conn->rollback();

var_dump($conn->query('select COUNT(*) from person')->fetch()->toArray(false));

?>
--EXPECTF--
int(1)
array(1) {
  [0]=>
  int(0)
}