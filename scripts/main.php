<?php
function test(){
    throw new Exception("foobar", 100500);
}

try {
    test();
} catch (Exception $e){
    $trace = $e->getTrace()[0];

    var_dump(empty($trace['file']));
    return 'success';
}

return 'fail';
