<?
$x = array('foobar');

for($i = 0; $i < 10000000; $i++){
    $str = "ab " . $i . " x ". $i . " abcd";
}

echo $str;