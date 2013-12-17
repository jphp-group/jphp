<?php

$z = 30;
$x = ($y = ($z += 30)) + 30;

var_dump($x, $y, $z);