--TEST--
Test weeak refs constructor
--FILE--
<?
$ref = new WeakReference();
--EXPECTF--

Fatal error: Direct instantiation of 'WeakReference' is not allowed, use WeakReference::create instead in %s on line 2, position 8