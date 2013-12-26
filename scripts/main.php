<?php



for($i=0;$i<1000000;$i++){
    try {
        throw new Exception("Message", $i);
    } catch (Exception $e) {
    }
}