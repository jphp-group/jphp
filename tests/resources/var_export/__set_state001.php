--FILE--
<?php
class A
{
    public $var1;
    protected $var2 = 40;
    private $var3 = 50;

    public static function __set_state($an_array) // С PHP 5.1.0
    {
        $obj = new A;
        $obj->var1 = $an_array['var1'];
        $obj->var2 = $an_array['var2'];
        $obj->var3 = $an_array['var3'];
        return $obj;
    }
}

$a = new A;
$a->var1 = 30;

eval('$b = ' . var_export($a, true) . ';'); // $b = A::__set_state(array(
//    'var1' => 5,
//    'var2' => 'foo',
// ));
var_dump($b);
--EXPECTF--
object(A)#%d (3) {
  ["var1"]=>
  int(30)
  ["var2":protected]=>
  int(40)
  ["var3":"A":private]=>
  int(50)
}