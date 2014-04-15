<?php

$a = [];
$a[]["foo"] = "foo";

if ($a[0]['foo'] != 'foo')
    return 'fail';

return 'success';
