<?php


class B {
    function doButtonAction($a)
    {
        //$a = ['a', 'b', 'c'];
        $this->method($a);
        var_dump($a); // NULL ???
    }

    public function method()
    {
        echo "foobar\n";
    }
}

$b = new B();
$b->doButtonAction(['a', 'b', 'c']);