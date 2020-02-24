--TEST--
Empty Heredoc string causes syntax error
--FILE--
<?php
$heredoc_empty_string = <<<EOD
EOD;
var_dump($heredoc_empty_string);
--EXPECTF--
string(0) ""