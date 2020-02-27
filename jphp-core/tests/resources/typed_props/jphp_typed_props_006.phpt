--TEST--
jphp no support ref for pass typed props
--FILE--
<?

class A {
    public int $x = 0;

    public function test(&$prop) {
        return $prop;
    }
}

$a = new A();
$x = $a->test($a->x);
$x = "foobar";
var_dump($a);
?>
--EXPECTF--

Fatal error: Unable to pass int as ref argument, jphp will not support this feature in %s on line 12, position %d
