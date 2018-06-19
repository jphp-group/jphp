--TEST--
Reflection Method -> bug #267
--FILE--
<?php

class Test{
    public $arr;

    function __construct(){
        var_dump($this->arr);
    }
}

function unserializeTo($data, string $className, $args = []){
    $class = new ReflectionClass($className);

    $instance = $class->newInstanceWithoutConstructor();

    if(is_object($data) || is_array($data)){
        foreach($data as $key => $value){
            if($class->hasProperty($key)){
                /** @var ReflectionProperty $property */
                $property = $class->getProperty($key);

                $property->setValue($instance, $value);
            }
        }
    }
    /** @var ReflectionMethod $constructor */
    $constructor = $class->getConstructor();
    if($constructor){
        $callback = $constructor->getClosure($instance);
        $callback(...$args);
    }
    return $instance;
}

unserializeTo(['arr' => [1, 2, 3]], Test::class); // вывод - array(0){}
unserializeTo(['arr' => (object)[1, 2, 3]], Test::class); // object(stdClass)#1686100174 (3) {1,2,3

?>
--EXPECTF--
array(3) {
  [0]=>
  int(1)
  [1]=>
  int(2)
  [2]=>
  int(3)
}
object(stdClass)#%d (3) {
  [0]=>
  int(1)
  [1]=>
  int(2)
  [2]=>
  int(3)
}