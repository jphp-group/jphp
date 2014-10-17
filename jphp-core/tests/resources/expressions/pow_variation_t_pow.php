--TEST--
Test T_POW operator : usage variations - different data types as $base argument
--FILE--
<?php

echo "*** Testing T_POW : usage variations ***\n";

if (!defined('PHP_INT_MAX')) {
    define('PHP_INT_MAX', 9223372036854775807);
}
//get an unset variable
$unset_var = 10;
unset ($unset_var);

// heredoc string
$heredoc = <<<EOT
abc
xyz
EOT;

// get a class
class classA
{
}

$inputs = array(
       // int data
/*1*/  0,
       1,
       12345,
       -2345,
       PHP_INT_MAX,

       // float data
/*6*/  10.5,
       -10.5,
       12.3456789000e10,
       12.3456789000E-10,
       .5,

       // null data
/*11*/ NULL,
       null,

       // boolean data
/*13*/ true,
       false,
       TRUE,
       FALSE,

       // empty data
/*17*/ "",
       '',
       array(),

       // string data
/*20*/ "abcxyz",
       'abcxyz',
       $heredoc,

       // object data
/*23*/ new classA(),

       // undefined data
/*24*/ @$undefined_var,

       // unset data
/*25*/ @$unset_var
);

// loop through each element of $inputs to check the behaviour of pow()
$iterator = 1;
foreach($inputs as $input) {
	echo "\n-- Iteration $iterator --\n";
	var_dump($input ** 3);
	$iterator++;
};
?>
===Done===
--EXPECTF--
*** Testing T_POW : usage variations ***

-- Iteration 1 --
int(0)

-- Iteration 2 --
int(1)

-- Iteration 3 --
int(1881365963625)

-- Iteration 4 --
int(-12895213625)

-- Iteration 5 --
float(7.8463771692334E+56)

-- Iteration 6 --
float(1157.625)

-- Iteration 7 --
float(-1157.625)

-- Iteration 8 --
float(1.8816763717892E+33)

-- Iteration 9 --
float(1.8816763717892E-27)

-- Iteration 10 --
float(0.125)

-- Iteration 11 --
int(0)

-- Iteration 12 --
int(0)

-- Iteration 13 --
int(1)

-- Iteration 14 --
int(0)

-- Iteration 15 --
int(1)

-- Iteration 16 --
int(0)

-- Iteration 17 --
int(0)

-- Iteration 18 --
int(0)

-- Iteration 19 --
int(0)

-- Iteration 20 --
int(0)

-- Iteration 21 --
int(0)

-- Iteration 22 --
int(0)

-- Iteration 23 --
int(0)

-- Iteration 24 --
int(0)

-- Iteration 25 --
int(0)
===Done===