<?php
use std;

$hello = new \helloworld\HelloWorld("Hello? World");
$hello->print();

$a = ['x' => ['a' => 2, 'b' => 3]];
$b = ['x' => ['b' => 4, 'd' => 5]];

$result = array_merge_recursive($b, $a);
print_r($result);