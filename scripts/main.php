<?

function test(&$var){
    $var = 222;
}

$mm = 20;
test($mm);
var_dump($mm);