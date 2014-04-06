<?php

class A {}

$class = 'A';
$a = new $class;
if (!($a instanceof A))
    return "fail_1: new with var";

$class = ['class' => 'A'];
$a = new $class['class'];

if (!($a instanceof A))
    return "fail_2: new with var-array";

$class = new stdClass();
$class->class = 'A';
$a = new $class->class;

if (!($a instanceof A))
    return "fail_3: new with var-property";

return "success";