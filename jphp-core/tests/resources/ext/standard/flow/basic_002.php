--TEST--
Basic cursor test with iterator
--FILE--
<?php

use php\util\Flow;

class MyIter implements Iterator {

    protected $i = 0;

    public function current() { return $this->i; }

    public function next() { $this->i++;  }

    public function key() { return $this->i; }

    public function valid() { return $this->i < 5;  }

    public function rewind() { $this->i = 0; }
}

$cursor = Flow::of(new MyIter());
foreach($cursor as $el) {
    var_dump($el);
}

echo "--\n";
foreach(new MyIter() as $el) {
    var_dump($el);
}

?>
--EXPECT--
int(0)
int(1)
int(2)
int(3)
int(4)
--
int(0)
int(1)
int(2)
int(3)
int(4)
