--TEST--
TextWord basic
--FILE--
<?php

use text\TextWord;

$text = new TextWord('hello world');

echo "levenshteinDistance\n";
var_dump($text->levenshteinDistance('hello world'));
var_dump($text->levenshteinDistance('helo wordl'));
var_dump($text->levenshteinDistance('hello worldd'));
var_dump($text->levenshteinDistance(' hello world'));

echo "cosineDistance\n";
var_dump(round($text->cosineDistance('hello world'), 2));
var_dump(round($text->cosineDistance('helo wordl'), 2));
var_dump(round($text->cosineDistance('hello worldd'), 2));
var_dump(round($text->cosineDistance(' hello world'), 2));

echo "hammingDistance\n";
var_dump($text->hammingDistance('hello world'));
var_dump($text->hammingDistance('holle wordl'));

echo "jaccardDistance\n";
var_dump(round($text->jaccardDistance('hello world'), 2));
var_dump(round($text->jaccardDistance('hello'), 2));
var_dump($text->jaccardDistance('world hello'));

echo "jaroWinklerDistance\n";
var_dump(round($text->jaroWinklerDistance('hello world'), 2));
var_dump(round($text->jaroWinklerDistance('helo wordl'), 2));
var_dump(round($text->jaroWinklerDistance('hello worldd'), 2));
var_dump(round($text->jaroWinklerDistance(' hello world'), 2));

echo "fuzzyScore\n";
var_dump($text->fuzzyScore('hello world'));
var_dump($text->fuzzyScore('hw'));
var_dump($text->fuzzyScore('abc'));


?>
--EXPECTF--
levenshteinDistance
int(0)
int(3)
int(1)
int(1)
cosineDistance
float(0)
float(1)
float(0.5)
float(0)
hammingDistance
int(0)
int(4)
jaccardDistance
float(0)
float(0.5)
float(0)
jaroWinklerDistance
float(1)
float(0.95)
float(1)
float(0.91)
fuzzyScore
int(31)
int(2)
int(0)