<?php

$x = function () use ($y, $z) {
    $my = 20;
    var_dump(get_defined_vars());
};

$x();