--TEST--
test bind #2
--FILE--
<?php
use php\sql\SqlDriverManager;
use php\sql\SqlResult;
use php\util\Flow;

SqlDriverManager::install('sqlite');

$conn = SqlDriverManager::getConnection('sqlite::memory:');

$conn->query('create table person (id integer, name string)')->update();
$st = $conn->query("insert into person values(?, ?)", [1, 'leo']);
$st->bind(1, 'foobar');
$st->update();

$array = Flow::of($conn->query('select * from person'))
    ->map(function (SqlResult $result) { return $result->get('name'); })
    ->toArray();

var_dump($array);

?>
--EXPECTF--
array(1) {
  [0]=>
  string(6) "foobar"
}