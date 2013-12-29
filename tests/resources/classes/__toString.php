<?php

class Foobar {

    public function __toString(){
        return 'success';
    }
}

$foobar = new Foobar();
if ($foobar . '' !== 'success')
    return 'fail_1';

return 'success';