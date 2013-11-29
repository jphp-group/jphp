<?

$arr['a'] = 'foobar';

for($i = 0; $i < 10000000; $i++){
    $x = $arr['a'];
}