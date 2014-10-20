--TEST--
Try .. Finally without catches with throwing Exception
--FILE--
<?php

function test(){
    try {
        try {
            throw new Exception();
        } finally {
            echo "First\n";
        }
        echo "Fail\n";
    } finally {
        echo "Last\n";
    }
}

try {
    test();
} catch (Exception $e) {

}

?>
--EXPECT--
First
Last
