<?

function test(){
    static $i = 20 + 30;
    $i++;
    return $i;
}

for($i = 0; $i < 10000000; $i++){
    $x = test();
}
echo $x;