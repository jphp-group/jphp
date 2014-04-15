--TEST--
Test doctype
--FILE--
<?php

function a(/** ... */) { }

function b(/**  */ $a /** ... */, $b = /** foobar */ 20) { }

class C { }
interface D {}

class A /** .. */ extends /** .. */ C implements  /* ... */ D /* ... */ {

}

if /** ... */(true) /** ... */ {
    echo "if_else_success\n";
} /** ... */ else /** ... */ {

}


switch /** .. */ (1) /** .. */ {
    /** ... */case /** ... */ 1 /** ... */: echo "switch_success\n";
}
?>
--EXPECT--
if_else_success
switch_success
