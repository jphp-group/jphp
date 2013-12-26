<?php

class X extends Exception {}
class Y extends X {}
class Z extends Y {}
class Z1 extends Z {}
class Z2 extends Z1 {}
class Z3 extends Z2 {}
class Z4 extends Z3 {}


function test(){
    $th = new Z4;

    for($i = 0; $i < 10000000; $i++){
        $x = ($th instanceof adgfdsgfds);
    }
}

test();