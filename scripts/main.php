<?php

class X extends Exception {}
class Y extends X {}

$th1 = new Y();
$th2 = new X();

for($i = 0; $i < 1000000; $i++){
    try {
        throw new Y;
    } catch (Y $e) {
        try {
            throw new X;
        } catch (X $e) { }
    }
}