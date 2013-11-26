<?
class Callback {

    public function __invoke($x){

    }
}

$func = new Callback();

for($i = 0; $i < 10000000; $i++){
    $func($i);
}