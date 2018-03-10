<?php

for($i = 0; $i < 2; $i++){
    $func[] = fn() => {
        static $i;
        $i++;
        return $i;
    };
}

$func[0]();
$x = $func[0]();
$func[1]();
$y = $func[1]();

if ($x !== 2)
    return "fail_x: $x != 2";

if ($y !== 2)
    return "fail_y: $y != 2";

return 'success';
