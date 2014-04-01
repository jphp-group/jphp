--TEST--
Levenshtein - basic 2
--FILE--
<?php

$input = 'carrrot';
$words  = array('apple','pineapple','banana','orange',
                'radish','carrot','pea','bean','potato');
foreach($words as $el){
    echo "- levenshtein($el, $input) -\n";
    var_dump(levenshtein($el, $input, 2, 4, 8));
}

--EXPECT--
- levenshtein(apple, carrrot) -
int(20)
- levenshtein(pineapple, carrrot) -
int(28)
- levenshtein(banana, carrrot) -
int(22)
- levenshtein(orange, carrrot) -
int(22)
- levenshtein(radish, carrrot) -
int(22)
- levenshtein(carrot, carrrot) -
int(2)
- levenshtein(pea, carrrot) -
int(20)
- levenshtein(bean, carrrot) -
int(22)
- levenshtein(potato, carrrot) -
int(18)
