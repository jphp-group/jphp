<?php

class My {
    function __destruct(){
        echo '1';
    }
}

for($i = 0; $i < 10; $i++){
    $my = new My();
}