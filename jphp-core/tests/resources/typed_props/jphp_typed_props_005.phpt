--TEST--
jphp no support ref for return typed props
--FILE--
<?

class A {
    public int $x = 0;

    public function &test() {
        return $this->x;
    }
}

$a = new A();
$x = $a->test();
$x = "foobar";
var_dump($a);
?>
--EXPECTF--

Fatal error: Unable to return int as ref, jphp will not support this feature in %s on line 7, position %d
