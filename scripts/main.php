<?

$a = array(1, 2, 3);

$i = 3;
foreach($a as &$key => $value){
    $key = $i;
    $i++;
}

print_r($a);
