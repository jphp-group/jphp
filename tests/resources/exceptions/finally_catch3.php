--TEST--
Try .. Finally with catch
--FILE--
<?php

try {
    try {
        $s = 'foobar';
    } finally {
        echo "First\n";
        throw new Exception("Last\n");
    } catch (Exception $e){
        echo $e->getMessage();
    }
} catch (Exception $e){
    echo "Success: ";
    echo $e->getMessage();
}


--EXPECT--
First
Success: Last
