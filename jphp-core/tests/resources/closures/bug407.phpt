--TEST--
Cannot bind closure
--FILE--
<?php
function &getVar($obj, string $var)
{
    return \Closure::bind(
        function & () use ($var) {
            return $this->{$var};
        },
        $obj,
        \get_class($obj)
    )();
}
class a { private $help = 'abc'; }

var_dump(getVar(new a, 'help'));
?>
--EXPECTF--
string(3) "abc"