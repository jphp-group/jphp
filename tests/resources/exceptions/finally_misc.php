--TEST--
Try .. Finally misc
--FILE--
<?php

try {

} finally {
    echo "First\n";
}

try {
    try {
        $s = "foobar";
    } finally {
        echo "Second\n";
    }
    echo "Third\n";
} finally {
    echo "Last\n";
}

var_dump($s);

--EXPECT--
First
Second
Third
Last
string(6) "foobar"
