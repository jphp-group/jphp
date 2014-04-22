<?php

/**
 * Class My
 */
class My {

    /**
     * @var int
     */
    public $x;
}

$r = new ReflectionClass('My');
echo $r->getProperty('x')->getDocComment();
