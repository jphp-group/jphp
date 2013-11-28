<?

$arr = 'HeLllO wOrLD YyES!';

for($i = 0; $i < 10000000; $i++)
    $str = ucwords($arr);

echo $str;
