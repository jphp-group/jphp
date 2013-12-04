<?

class Foo {
  public function __get($name) {
    var_dump($name);
    if ($name != 'bar')
      return $this->bar;
    else
      return null;
  }
}
$f = new Foo;
$f->foo;