--TEST--
SqlDriverManager test metadata #2
--FILE--
<?php
use php\sql\SqlDriverManager;

SqlDriverManager::install('sqlite');

$conn = SqlDriverManager::getConnection('sqlite::memory:', ['username' => 'admin', 'password' => 'admin']);

var_dump($conn->getCatalogs());
var_dump($conn->getSchemas());
var_dump($conn->getMetaData());

?>
--EXPECT--
array(0) {
}
array(0) {
}
array(34) {
  ["userName"]=>
  NULL
  ["driverName"]=>
  string(10) "SQLiteJDBC"
  ["driverVersion"]=>
  string(6) "native"
  ["databaseName"]=>
  string(6) "SQLite"
  ["databaseVersion"]=>
  string(5) "3.8.7"
  ["catalogSeparator"]=>
  string(1) "."
  ["catalogTerm"]=>
  string(7) "catalog"
  ["schemaTerm"]=>
  string(6) "schema"
  ["procedureTerm"]=>
  string(15) "not_implemented"
  ["searchStringEscape"]=>
  NULL
  ["numericFunctions"]=>
  string(0) ""
  ["stringFunctions"]=>
  string(0) ""
  ["timeDateFunctions"]=>
  string(0) ""
  ["systemFunctions"]=>
  string(0) ""
  ["defaultTransactionIsolation"]=>
  int(8)
  ["identifierQuoteString"]=>
  string(1) " "
  ["maxBinaryLiteralLength"]=>
  int(0)
  ["maxCatalogNameLength"]=>
  int(0)
  ["maxCharLiteralLength"]=>
  int(0)
  ["maxConnections"]=>
  int(0)
  ["maxColumnNameLength"]=>
  int(0)
  ["maxColumnsInGroupBy"]=>
  int(0)
  ["maxColumnsInIndex"]=>
  int(0)
  ["maxColumnsInOrderBy"]=>
  int(0)
  ["maxColumnsInSelect"]=>
  int(0)
  ["maxColumnsInTable"]=>
  int(0)
  ["maxCursorNameLength"]=>
  int(0)
  ["maxIndexLength"]=>
  int(0)
  ["maxProcedureNameLength"]=>
  int(0)
  ["maxRowSize"]=>
  int(0)
  ["maxSchemaNameLength"]=>
  int(0)
  ["maxStatementLength"]=>
  int(0)
  ["maxTableNameLength"]=>
  int(0)
  ["maxTablesInSelect"]=>
  int(0)
}