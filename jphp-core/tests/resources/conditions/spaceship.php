<?php

// Integers
if (1 <=> 1 !== 0) return "fail_1";
if (1 <=> 2 !== -1) return "fail_2";
if (2 <=> 1 !== 1) return "fail_3";

// Floats
if (1.5 <=> 1.5 !== 0) return "fail_4"; // 0
if (1.5 <=> 2.5 !== -1) return "fail_5"; // -1
if (2.5 <=> 1.5 !== 1) return "fail_6"; // 1

// Strings
if ("a" <=> "a" !== 0) return "fail_7"; // 0
if ("a" <=> "b" !== -1) return "fail_8"; // -1
if ("b" <=> "a" !== 1) return 'fail_9'; // 1

if ("a" <=> "aa" !== -1) return "fail_10"; // -1
if ("zz" <=> "aa" !== 1) return "fail_11"; // 1

// Arrays
if ([] <=> [] !== 0) return "fail_12"; // 0
if ([1, 2, 3] <=> [1, 2, 3] !== 0) return "fail_13"; // 0
if ([1, 2, 3] <=> [] !== 1) return "fail_14"; // 1
if ([1, 2, 3] <=> [1, 2, 1] !== 1) return "fail_15"; // 1
if ([1, 2, 3] <=> [1, 2, 4] !== -1) return "fail_16"; // -1

// Objects
$a = (object) ["a" => "b"];
$b = (object) ["a" => "b"];
if ($a <=> $b !== 0) return "fail_17"; // 0

$a = (object) ["a" => "b"];
$b = (object) ["a" => "c"];
if ($a <=> $b !== -1) return "fail_18"; // -1

$a = (object) ["a" => "c"];
$b = (object) ["a" => "b"];
if ($a <=> $b !== 1) return "fail_19"; // 1

$a = (object) ["a" => "b"];
$b = (object) ["b" => "b"];
if ($a <=> $b !== 1) return "fail_20"; // 0

return "success";