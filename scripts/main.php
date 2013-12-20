<?php

$array = array("size" => "XL", "color" => "gold");
$x = array_values($array);
$x[0] = 111;

print_r($x);
print_r($array);
