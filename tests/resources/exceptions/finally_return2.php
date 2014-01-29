--TEST--
Try .. Finally with return
--FILE--
<?php

function test(){
    try {
        try {
            return "success";
        } finally {
            echo "First\n";
            throw new Exception("test");
        }
    } catch(Exception $e){
        echo "Exception\n";
    } finally {
        echo "Last\n";
    }
}

var_dump(test());

--EXPECT--
First
Exception
Last
NULL
