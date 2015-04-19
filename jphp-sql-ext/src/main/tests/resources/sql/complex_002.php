--TEST--
SqlDriverManager test insert into table #2
--FILE--
<?php
use php\sql\SqlDriverManager;
use php\sql\SqlResult;
use php\util\Flow;

SqlDriverManager::install('sqlite');

$conn = SqlDriverManager::getConnection('sqlite::memory:');

$conn->query('create table person (id integer, name string)')->update();
$result = $conn->query("insert into person values(?, ?)", [1, 'leo'])->update();
$result += $conn->query("insert into person values(?, ?)", [2, 'yui'])->update();

var_dump($result);

$array = Flow::of($conn->query('select * from person'))
    ->map(function (SqlResult $result) { return $result->toArray(); })
    ->toArray();
var_dump($array);

?>
--EXPECTF--
int(2)
array(2) {
  [0]=>
  array(2) {
    ["id"]=>
    int(1)
    ["name"]=>
    string(3) "leo"
  }
  [1]=>
  array(2) {
    ["id"]=>
    int(2)
    ["name"]=>
    string(3) "yui"
  }
}