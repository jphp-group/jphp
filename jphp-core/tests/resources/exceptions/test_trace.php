<?php

function test(){
    throw new Exception("foobar", 100500);
}

try {
    test();
} catch (Exception $e){
    if ($e->getLine() !== 4)
        return 'fail_line: 4 != ' . $e->getLine();

    if ($e->getMessage() !== "foobar")
        return 'fail_message';

    if ($e->getCode() !== 100500)
        return 'fail_code';

    $trace = $e->getTrace()[0];
    if ($trace['function'] !== 'test')
        return 'fail_function';

    if ($trace['line'] !== 8)
        return 'fail_line: 8 != ' . $trace['line'];

    if (empty($trace['file']))
        return 'fail_file: ' . $trace['file'];

    return 'success';
}

return 'fail';
