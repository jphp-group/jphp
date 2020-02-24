--TEST--
Test weeak refs with GC
--FILE--
<?
$t = new stdClass();
$ref = WeakReference::create($t);

var_dump($t);
$t = null;
\php\lang\System::gc();
var_dump($ref->get());
--EXPECTF--
object(stdClass)#%d (0) {
}
NULL