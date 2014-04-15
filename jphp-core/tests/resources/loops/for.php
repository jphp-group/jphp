<?php

$z = 10;
for($i = 0; $i < 10; $i++){
    if ($i == 8)
        break;

    if ($i == 5)
        continue;

    $z++;
}

for($j = 0; $j < 10; $j++);

for (;false;) {
    return 'fail_2';
}

for(;false;$a++) {
    return 'fail_3';
}

for($b = 100500;false;);

if ($b !== 100500)
    return 'fail_4';


return $i == 8 && $z == 17 && $j == 10 ? 'success' : 'fail';
