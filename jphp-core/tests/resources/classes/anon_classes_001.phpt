--TEST--
Test annon classes
--FILE--
<?
$identicalAnonClasses = [];

for ($i = 0; $i < 2; $i++) {
    $identicalAnonClasses[$i] = new class(99) {
        public $i;
        public function __construct($i) {
            $this->i = $i;
        }
    };
}

var_dump($identicalAnonClasses[0] == $identicalAnonClasses[1]); // true

$identicalAnonClasses[2] = new class(99) {
    public $i;
    public function __construct($i) {
        $this->i = $i;
    }
};

var_dump($identicalAnonClasses[0] == $identicalAnonClasses[2]); // false
?>
--EXPECTF--
bool(true)
bool(false)