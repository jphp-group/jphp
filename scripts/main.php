<?php

function handler($e){
    echo $e;
}

function exception_error_handler($errno, $errstr, $errfile, $errline ) {
    throw new ErrorException($errstr, $errno, 0, $errfile, $errline);
}
set_error_handler("exception_error_handler");
set_exception_handler('handler');

try {
    foreach($x as $el);
} catch (ErrorException $e){
    echo "URA!";
}