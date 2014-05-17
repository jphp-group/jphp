--TEST--
Issue #133
https://github.com/jphp-compiler/jphp/issues/133
--FILE--
<?php

define('DEF_CONST', 2);

class a {
	private $c = 0;
    public function b() {
    	$a = 1;
        switch($a) {
            case'1':
                $this->c = DEF_CONST;
        }
        echo $this->c;
    }
}
$a = new a();
$a->b();
--EXPECT--
2