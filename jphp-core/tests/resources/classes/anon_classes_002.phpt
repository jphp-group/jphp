--TEST--
Test annon classes
--FILE--
<?
var_dump(new class(20) {
    public function __construct($i) {
        $this->i = $i;
    }
});
?>
--EXPECTF--
object(class@anonymous)#%d (1) {
  ["i"]=>
  int(20)
}