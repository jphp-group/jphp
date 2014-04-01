--TEST--
Levenshtein - basic 2
--FILE--
<?php

$input = 'carrrot';
$words  = array('apple','pineapple','banana','orange',
                'radish','carrot','pea','bean','potato');
foreach($words as $el){
    echo "- levenshtein($el, $input) -\n";
    var_dump(levenshtein($el, $input));
}

--EXPECT--
- levenshtein(apple, carrrot) -
int(6)
- levenshtein(pineapple, carrrot) -
int(9)
- levenshtein(banana, carrrot) -
int(6)
- levenshtein(orange, carrrot) -
int(6)
- levenshtein(radish, carrrot) -
int(6)
- levenshtein(carrot, carrrot) -
int(1)
- levenshtein(pea, carrrot) -
int(7)
- levenshtein(bean, carrrot) -
int(7)
- levenshtein(potato, carrrot) -
int(6)
