<?

function &test(&$a) {
    return $a;
}

$x['foo'] = 100500;
$y =& test($x['foo']);
$y = 333;
print_r($x);