--TEST--
Test << operator : various numbers as strings
--SKIPIF--
<?php
if (PHP_INT_SIZE != 4) die("skip this test is for 32bit platform only");
?>
--FILE--
<?php

$strVals = array(
   "0","65", "1.2", "abc", "123abc", "123e5", "123e5xyz", " 123abc", "123 abc", "123abc ", "3.4a",
   "a5.9"
);

error_reporting(E_ERROR);

foreach ($strVals as $strVal) {
   foreach($strVals as $otherVal) {
      echo "--- testing: '$strVal' << '$otherVal' ---\n";
      var_dump(bin2hex($strVal<<$otherVal));
   }
}


?>
===DONE===
--EXPECT--
--- testing: '0' << '0' ---
string(2) "30"
--- testing: '0' << '65' ---
string(2) "30"
--- testing: '0' << '1.2' ---
string(2) "30"
--- testing: '0' << 'abc' ---
string(2) "30"
--- testing: '0' << '123abc' ---
string(2) "30"
--- testing: '0' << '123e5' ---
string(2) "30"
--- testing: '0' << '123e5xyz' ---
string(2) "30"
--- testing: '0' << ' 123abc' ---
string(2) "30"
--- testing: '0' << '123 abc' ---
string(2) "30"
--- testing: '0' << '123abc ' ---
string(2) "30"
--- testing: '0' << '3.4a' ---
string(2) "30"
--- testing: '0' << 'a5.9' ---
string(2) "30"
--- testing: '65' << '0' ---
string(4) "3635"
--- testing: '65' << '65' ---
string(6) "313330"
--- testing: '65' << '1.2' ---
string(6) "313330"
--- testing: '65' << 'abc' ---
string(4) "3635"
--- testing: '65' << '123abc' ---
string(36) "353736343630373532333033343233343838"
--- testing: '65' << '123e5' ---
string(36) "353736343630373532333033343233343838"
--- testing: '65' << '123e5xyz' ---
string(36) "353736343630373532333033343233343838"
--- testing: '65' << ' 123abc' ---
string(36) "353736343630373532333033343233343838"
--- testing: '65' << '123 abc' ---
string(36) "353736343630373532333033343233343838"
--- testing: '65' << '123abc ' ---
string(36) "353736343630373532333033343233343838"
--- testing: '65' << '3.4a' ---
string(6) "353230"
--- testing: '65' << 'a5.9' ---
string(4) "3635"
--- testing: '1.2' << '0' ---
string(2) "31"
--- testing: '1.2' << '65' ---
string(2) "32"
--- testing: '1.2' << '1.2' ---
string(2) "32"
--- testing: '1.2' << 'abc' ---
string(2) "31"
--- testing: '1.2' << '123abc' ---
string(36) "353736343630373532333033343233343838"
--- testing: '1.2' << '123e5' ---
string(36) "353736343630373532333033343233343838"
--- testing: '1.2' << '123e5xyz' ---
string(36) "353736343630373532333033343233343838"
--- testing: '1.2' << ' 123abc' ---
string(36) "353736343630373532333033343233343838"
--- testing: '1.2' << '123 abc' ---
string(36) "353736343630373532333033343233343838"
--- testing: '1.2' << '123abc ' ---
string(36) "353736343630373532333033343233343838"
--- testing: '1.2' << '3.4a' ---
string(2) "38"
--- testing: '1.2' << 'a5.9' ---
string(2) "31"
--- testing: 'abc' << '0' ---
string(2) "30"
--- testing: 'abc' << '65' ---
string(2) "30"
--- testing: 'abc' << '1.2' ---
string(2) "30"
--- testing: 'abc' << 'abc' ---
string(2) "30"
--- testing: 'abc' << '123abc' ---
string(2) "30"
--- testing: 'abc' << '123e5' ---
string(2) "30"
--- testing: 'abc' << '123e5xyz' ---
string(2) "30"
--- testing: 'abc' << ' 123abc' ---
string(2) "30"
--- testing: 'abc' << '123 abc' ---
string(2) "30"
--- testing: 'abc' << '123abc ' ---
string(2) "30"
--- testing: 'abc' << '3.4a' ---
string(2) "30"
--- testing: 'abc' << 'a5.9' ---
string(2) "30"
--- testing: '123abc' << '0' ---
string(6) "313233"
--- testing: '123abc' << '65' ---
string(6) "323436"
--- testing: '123abc' << '1.2' ---
string(6) "323436"
--- testing: '123abc' << 'abc' ---
string(6) "313233"
--- testing: '123abc' << '123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc' << '123e5' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc' << '123e5xyz' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc' << ' 123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc' << '123 abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc' << '123abc ' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc' << '3.4a' ---
string(6) "393834"
--- testing: '123abc' << 'a5.9' ---
string(6) "313233"
--- testing: '123e5' << '0' ---
string(6) "313233"
--- testing: '123e5' << '65' ---
string(6) "323436"
--- testing: '123e5' << '1.2' ---
string(6) "323436"
--- testing: '123e5' << 'abc' ---
string(6) "313233"
--- testing: '123e5' << '123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5' << '123e5' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5' << '123e5xyz' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5' << ' 123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5' << '123 abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5' << '123abc ' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5' << '3.4a' ---
string(6) "393834"
--- testing: '123e5' << 'a5.9' ---
string(6) "313233"
--- testing: '123e5xyz' << '0' ---
string(6) "313233"
--- testing: '123e5xyz' << '65' ---
string(6) "323436"
--- testing: '123e5xyz' << '1.2' ---
string(6) "323436"
--- testing: '123e5xyz' << 'abc' ---
string(6) "313233"
--- testing: '123e5xyz' << '123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5xyz' << '123e5' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5xyz' << '123e5xyz' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5xyz' << ' 123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5xyz' << '123 abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5xyz' << '123abc ' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123e5xyz' << '3.4a' ---
string(6) "393834"
--- testing: '123e5xyz' << 'a5.9' ---
string(6) "313233"
--- testing: ' 123abc' << '0' ---
string(6) "313233"
--- testing: ' 123abc' << '65' ---
string(6) "323436"
--- testing: ' 123abc' << '1.2' ---
string(6) "323436"
--- testing: ' 123abc' << 'abc' ---
string(6) "313233"
--- testing: ' 123abc' << '123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: ' 123abc' << '123e5' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: ' 123abc' << '123e5xyz' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: ' 123abc' << ' 123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: ' 123abc' << '123 abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: ' 123abc' << '123abc ' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: ' 123abc' << '3.4a' ---
string(6) "393834"
--- testing: ' 123abc' << 'a5.9' ---
string(6) "313233"
--- testing: '123 abc' << '0' ---
string(6) "313233"
--- testing: '123 abc' << '65' ---
string(6) "323436"
--- testing: '123 abc' << '1.2' ---
string(6) "323436"
--- testing: '123 abc' << 'abc' ---
string(6) "313233"
--- testing: '123 abc' << '123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123 abc' << '123e5' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123 abc' << '123e5xyz' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123 abc' << ' 123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123 abc' << '123 abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123 abc' << '123abc ' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123 abc' << '3.4a' ---
string(6) "393834"
--- testing: '123 abc' << 'a5.9' ---
string(6) "313233"
--- testing: '123abc ' << '0' ---
string(6) "313233"
--- testing: '123abc ' << '65' ---
string(6) "323436"
--- testing: '123abc ' << '1.2' ---
string(6) "323436"
--- testing: '123abc ' << 'abc' ---
string(6) "313233"
--- testing: '123abc ' << '123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc ' << '123e5' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc ' << '123e5xyz' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc ' << ' 123abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc ' << '123 abc' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc ' << '123abc ' ---
string(40) "2d32383832333033373631353137313137343430"
--- testing: '123abc ' << '3.4a' ---
string(6) "393834"
--- testing: '123abc ' << 'a5.9' ---
string(6) "313233"
--- testing: '3.4a' << '0' ---
string(2) "33"
--- testing: '3.4a' << '65' ---
string(2) "36"
--- testing: '3.4a' << '1.2' ---
string(2) "36"
--- testing: '3.4a' << 'abc' ---
string(2) "33"
--- testing: '3.4a' << '123abc' ---
string(38) "31373239333832323536393130323730343634"
--- testing: '3.4a' << '123e5' ---
string(38) "31373239333832323536393130323730343634"
--- testing: '3.4a' << '123e5xyz' ---
string(38) "31373239333832323536393130323730343634"
--- testing: '3.4a' << ' 123abc' ---
string(38) "31373239333832323536393130323730343634"
--- testing: '3.4a' << '123 abc' ---
string(38) "31373239333832323536393130323730343634"
--- testing: '3.4a' << '123abc ' ---
string(38) "31373239333832323536393130323730343634"
--- testing: '3.4a' << '3.4a' ---
string(4) "3234"
--- testing: '3.4a' << 'a5.9' ---
string(2) "33"
--- testing: 'a5.9' << '0' ---
string(2) "30"
--- testing: 'a5.9' << '65' ---
string(2) "30"
--- testing: 'a5.9' << '1.2' ---
string(2) "30"
--- testing: 'a5.9' << 'abc' ---
string(2) "30"
--- testing: 'a5.9' << '123abc' ---
string(2) "30"
--- testing: 'a5.9' << '123e5' ---
string(2) "30"
--- testing: 'a5.9' << '123e5xyz' ---
string(2) "30"
--- testing: 'a5.9' << ' 123abc' ---
string(2) "30"
--- testing: 'a5.9' << '123 abc' ---
string(2) "30"
--- testing: 'a5.9' << '123abc ' ---
string(2) "30"
--- testing: 'a5.9' << '3.4a' ---
string(2) "30"
--- testing: 'a5.9' << 'a5.9' ---
string(2) "30"
===DONE===