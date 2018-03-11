--TEST--
SqlDriverManager test connection #1
--FILE--
<?php

use php\sql\SqlDriverManager;

SqlDriverManager::install('sqlite');

$conn = SqlDriverManager::getConnection('sqlite::memory:');

var_dump("readonly", $conn->readOnly);
var_dump("catalog", $conn->catalog);
var_dump("autoCommit", $conn->autoCommit);
var_dump("transactionIsolation", $conn->transactionIsolation);

?>
--EXPECTF--
string(8) "readonly"
bool(false)
string(7) "catalog"
NULL
string(10) "autoCommit"
bool(true)
string(20) "transactionIsolation"
int(8)