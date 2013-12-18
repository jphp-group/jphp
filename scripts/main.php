<?php

function test_StringManipulation($count = 130000) {
    $time_start = microtime(true);
    $stringFunctions = array("addslashes", "chunk_split", "metaphone", "strip_tags", "md5", "sha1", "strtoupper", "strtolower", "strrev", "strlen", "soundex", "ord");
    foreach ($stringFunctions as $key => $function) {
        if (!function_exists($function))
            unset($stringFunctions[$key]);
    }
    $string = "the quick brown fox jumps over the lazy dog";
    for ($i=0; $i < $count; $i++) {
        foreach ($stringFunctions as $function) {
            $r = @call_user_func_array($function, array($string));
        }
    }
    return number_format(microtime(true) - $time_start, 3);
}

test_StringManipulation();