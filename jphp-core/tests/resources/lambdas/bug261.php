<?php

$fn = fn() => {
    $GLOBALS['x'] = 20;
};

$fn();

return $GLOBALS['x'] === 20 ? 'success' : 'fail';