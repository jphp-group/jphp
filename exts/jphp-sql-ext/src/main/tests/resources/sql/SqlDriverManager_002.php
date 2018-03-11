--TEST--
SqlDriverManager test install invalid #2
--FILE--
<?php

use php\sql\SqlDriverManager;
use php\sql\SqlException;

try {
    SqlDriverManager::install('foobar');
} catch (SqlException $e) {
    echo $e->getMessage();
}

?>
--EXPECT--
java.sql.SQLException: Driver class 'foobar' is not found in classpath