<?

$array1 = array();
$array1["color"] = "red";
$array1[0] = 2;
$array1[1] = 4;

$array2 = array("a", "b");
$array2["color"] = "green";
$array2["shape"] = "trapezoid";
$array2[2] = 4;

$result = array_merge($array1, $array2);
$result['xxx'] = $array2;

$obj = new stdClass();
$obj->x = 20;
$obj->y = 40;

var_dump($obj);