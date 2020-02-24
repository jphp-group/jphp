--TEST--
Test weeak refs with closure
--FILE--
<?
$y = 0;
$fn = fn($x) => $x + $y;
$ref = WeakReference::create($fn);

var_dump($fn);
$fn = null;
\php\lang\System::gc();
var_dump($ref->get());
--EXPECTF--
object(Closure)#%d (1) {
  ["uses"]=>
  array(1) {
    [0]=>
    int(0)
  }
}
NULL