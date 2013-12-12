<?php

function test(&$item, $def){
    $item = 100500;
    $def = 100500;
}

$arr = array('x' => 200600, 'y' => 200600);
test($arr['x'], $arr['y']);

return $arr['x'] . '|' . $arr['y'];
