<?php

class MyException  {

    public $x = 20;
    public $y = 20;
    public $z = 20;
    public $a = 20;
    public $b = 'aaaa';
    public $c = 'bbbb';
    public $d = array(1, 2, 3);
}

for($i = 0; $i < 1000000; $i++)
    $e = new MyException();