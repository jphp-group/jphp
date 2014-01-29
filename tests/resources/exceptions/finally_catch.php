--TEST--
Try .. Finally with catch
--FILE--
<?php

try {
    throw new Exception("First\n");
} finally {
    echo "Last\n";
} catch (Exception $e){
    echo $e->getMessage();
}


--EXPECT--
First
Last
