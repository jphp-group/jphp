<?php

$x = 20;
$name = 'x';

if ($$name !== 20)
    return 'fail_1: $\'x\' != 20';

if (${$name . ''} !== 20)
    return 'fail_2: ${\'x\'} != 20';

unset(${$name});

if (isset($x))
    return 'fail_3: unset($x)';

$foo = isset(${'x'});
$var = 'x';
$bar = isset($$var);

$y = 'name';
$$$y = 30;

if ($x !== 30)
    return 'fail_4: $$$x != 30';


function test($x, $name){
    return ${$name};
}

if (test(100, 'x') !== 100)
    return 'fail_5: test(100, x) != 100';

return 'success';
