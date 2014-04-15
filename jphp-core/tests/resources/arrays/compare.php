--FILE--
<?php
function bool2str($bool)
{
    if ($bool === false) {
        return 'FALSE';
    } else {
        return 'TRUE';
    }
}

function compareObjects(&$o1, &$o2)
{
    echo 'o1 == o2 : ' . bool2str($o1 == $o2) . "\n";
    echo 'o1 != o2 : ' . bool2str($o1 != $o2) . "\n";
    echo 'o1 === o2 : ' . bool2str($o1 === $o2) . "\n";
    echo 'o1 !== o2 : ' . bool2str($o1 !== $o2) . "\n";
}

class Flag
{
    public $flag;

    function Flag($flag = true) {
        $this->flag = $flag;
    }
}

class OtherFlag
{
    public $flag;

    function OtherFlag($flag = true) {
        $this->flag = $flag;
    }
}

$o = new Flag();
$p = new Flag();
$q = $o;
$r = new OtherFlag();

echo "Two instances of the same class\n";
compareObjects($o, $p);

echo "\nTwo references to the same instance\n";
compareObjects($o, $q);

echo "\nInstances of two different classes\n";
compareObjects($o, $r);

$arr['x'] =& $arr;
var_dump($arr == $arr);


$a = array("a" => "apple", "b" => "1");
$b = array("a" => "apple", "b" => "1");
$c = array("b" => 1, "a" => "apple");
$d = array("b" => "1", "a" => "apple");

echo "Simple equal 1: "; var_dump($a == $b);
echo "Simple equal 2: "; var_dump($a == $c);
echo "Simple equal 3: "; var_dump($a == $d);

echo "Strict equal 1: "; var_dump($a === $b);
echo "Strict equal 2: "; var_dump($a === $c);
echo "Strict equal 3: "; var_dump($a === $d);
?>
--EXPECT--
Two instances of the same class
o1 == o2 : TRUE
o1 != o2 : FALSE
o1 === o2 : FALSE
o1 !== o2 : TRUE

Two references to the same instance
o1 == o2 : TRUE
o1 != o2 : FALSE
o1 === o2 : TRUE
o1 !== o2 : FALSE

Instances of two different classes
o1 == o2 : FALSE
o1 != o2 : TRUE
o1 === o2 : FALSE
o1 !== o2 : TRUE
bool(true)
Simple equal 1: bool(true)
Simple equal 2: bool(true)
Simple equal 3: bool(true)
Strict equal 1: bool(true)
Strict equal 2: bool(false)
Strict equal 3: bool(false)
