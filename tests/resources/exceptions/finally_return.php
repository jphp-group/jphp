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
        }
    } finally {
        echo "Last\n";
    }
}

var_dump(test());

--EXPECT--
First
Last
string(7) "success"
