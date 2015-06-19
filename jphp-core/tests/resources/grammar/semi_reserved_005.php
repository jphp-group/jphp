--TEST--
Test semi-reserved words as class constants
--FILE--
<?php

class Obj
{
    const EMPTY = 'empty';
    const CALLABLE = 'callable';
    const TRAIT = 'trait';
    const EXTENDS = 'extends';
    const IMPLEMENTS = 'implements';
    const CONST = 'const';
    const ENDDECLARE = 'enddeclare';
    const ENDFOR = 'endfor';
    const ENDFOREACH = 'endforeach';
    const ENDIF = 'endif';
    const ENDWHILE = 'endwhile';
    const AND = 'and';
    const GLOBAL = 'global';
    const GOTO = 'goto';
    const INSTANCEOF = 'instanceof';
    const INSTEADOF = 'insteadof';
    const INTERFACE = 'interface';
    const NAMESPACE = 'namespace';
    const NEW = 'new';
    const OR = 'or';
    const XOR = 'xor';
    const TRY = 'try';
    const USE = 'use';
    const VAR = 'var';
    const EXIT = 'exit';
    const LIST = 'list';
    const CLONE = 'clone';
    const INCLUDE = 'include';
    const INCLUDE_ONCE = 'include_once';
    const THROW = 'throw';
    const ARRAY = 'array';
    const PRINT = 'print';
    const ECHO = 'echo';
    const REQUIRE = 'require';
    const REQUIRE_ONCE = 'require_once';
    const RETURN = 'return';
    const ELSE = 'else';
    const ELSEIF = 'elseif';
    const DEFAULT = 'default';
    const BREAK = 'break';
    const CONTINUE = 'continue';
    const SWITCH = 'switch';
    const YIELD = 'yield';
    const FUNCTION = 'function';
    const IF = 'if';
    const ENDSWITCH = 'endswitch';
    const FINALLY = 'finally';
    const FOR = 'for';
    const FOREACH = 'foreach';
    const DECLARE = 'declare';
    const CASE = 'case';
    const DO = 'do';
    const WHILE = 'while';
    const AS = 'as';
    const CATCH = 'catch';
    const DIE = 'die';
    const SELF = 'self';
    const PARENT = 'parent';
}

echo Obj::EMPTY, "\n";
echo Obj::CALLABLE, "\n";
echo Obj::TRAIT, "\n";
echo Obj::EXTENDS, "\n";
echo Obj::IMPLEMENTS, "\n";
echo Obj::CONST, "\n";
echo Obj::ENDDECLARE, "\n";
echo Obj::ENDFOR, "\n";
echo Obj::ENDFOREACH, "\n";
echo Obj::ENDIF, "\n";
echo Obj::ENDWHILE, "\n";
echo Obj::AND, "\n";
echo Obj::GLOBAL, "\n";
echo Obj::GOTO, "\n";
echo Obj::INSTANCEOF, "\n";
echo Obj::INSTEADOF, "\n";
echo Obj::INTERFACE, "\n";
echo Obj::NAMESPACE, "\n";
echo Obj::NEW, "\n";
echo Obj::OR, "\n";
echo Obj::XOR, "\n";
echo Obj::TRY, "\n";
echo Obj::USE, "\n";
echo Obj::VAR, "\n";
echo Obj::EXIT, "\n";
echo Obj::LIST, "\n";
echo Obj::CLONE, "\n";
echo Obj::INCLUDE, "\n";
echo Obj::INCLUDE_ONCE, "\n";
echo Obj::THROW, "\n";
echo Obj::ARRAY, "\n";
echo Obj::PRINT, "\n";
echo Obj::ECHO, "\n";
echo Obj::REQUIRE, "\n";
echo Obj::REQUIRE_ONCE, "\n";
echo Obj::RETURN, "\n";
echo Obj::ELSE, "\n";
echo Obj::ELSEIF, "\n";
echo Obj::DEFAULT, "\n";
echo Obj::BREAK, "\n";
echo Obj::CONTINUE, "\n";
echo Obj::SWITCH, "\n";
echo Obj::YIELD, "\n";
echo Obj::FUNCTION, "\n";
echo Obj::IF, "\n";
echo Obj::ENDSWITCH, "\n";
echo Obj::FINALLY, "\n";
echo Obj::FOR, "\n";
echo Obj::FOREACH, "\n";
echo Obj::DECLARE, "\n";
echo Obj::CASE, "\n";
echo Obj::DO, "\n";
echo Obj::WHILE, "\n";
echo Obj::AS, "\n";
echo Obj::CATCH, "\n";
echo Obj::DIE, "\n";
echo Obj::SELF, "\n";
echo Obj::PARENT, "\n";

echo "\nDone\n";

?>
--EXPECTF--
empty
callable
trait
extends
implements
const
enddeclare
endfor
endforeach
endif
endwhile
and
global
goto
instanceof
insteadof
interface
namespace
new
or
xor
try
use
var
exit
list
clone
include
include_once
throw
array
print
echo
require
require_once
return
else
elseif
default
break
continue
switch
yield
function
if
endswitch
finally
for
foreach
declare
case
do
while
as
catch
die
self
parent

Done