<?
namespace foo\bar;

for($i = 0; $i < 10000000; $i++){
    $x = \max(array(1, $i, 4, 5, 6));
}

echo $x;