<?php

$z = 10;
for($i = 0; $i < 10; $i++){
    if ($i == 8)
        break;

    if ($i == 5)
        continue;

    $z++;
}


return $i == 8 && $z == 17 ? 'success' : 'fail';
