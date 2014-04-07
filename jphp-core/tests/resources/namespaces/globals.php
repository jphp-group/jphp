--TEST--
Test namespaces for invoction
--FILE--
<?php

namespace {
    function a() {
        echo "function_success", "\n";
    }

    const FOOBAR = "const_success\n";
}

namespace Foo {
    const FOOBAR = "Foo\\FOOBAR_success\n";

    a();
    \a();

    echo FOOBAR;
    echo \FOOBAR;

    include __DIR__ . '/globals.inc';

    echo FOOBAR2;
    echo \FOOBAR2;

    echo bar\FOOBAR;
    echo \bar\FOOBAR;
}
?>
--EXPECTF--
function_success
function_success
Foo\FOOBAR_success
const_success
Notice: Use of undefined constant Foo\FOOBAR2 - assumed 'Foo\FOOBAR2' in %s on line %d at pos %d
FOOBAR2const2_success
Notice: Use of undefined constant Foo\bar\FOOBAR - assumed 'Foo\bar\FOOBAR' in %s on line %d at pos %d
FOOBARbar\FOOBAR_success
