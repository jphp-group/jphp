--TEST--
Try .. Finally with catch
--FILE--
<?php

try {
    try {
        throw new Exception("First exception\n");
    } finally {
        echo "First\n";
    } catch (Foobar $e){
        echo $e->getMessage();
    }
} catch (Exception $e){
    echo "Success: ";
    echo get_class($e);
}


--EXPECT--
First
Success: Exception
