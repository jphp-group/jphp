--TEST--
Bug #127
--FILE--
<?php
$c = new stdClass();
$c->d = array('cls' => 'stdClass');

$e = 'cls';
$b = new stdClass();

$a = true;

if ($a && ($b instanceof $c->d[$e])) {
    echo "success";
    return;
}
echo "fail";
?>
--EXPECT--
success
