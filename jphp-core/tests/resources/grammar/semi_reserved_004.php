--TEST--
Test semi-reserved words as static class properties
--FILE--
<?php

class Obj
{
    static $empty = 'empty';
    static $callable = 'callable';
    static $class = 'class';
    static $trait = 'trait';
    static $extends = 'extends';
    static $implements = 'implements';
    static $static = 'static';
    static $abstract = 'abstract';
    static $final = 'final';
    static $public = 'public';
    static $protected = 'protected';
    static $private = 'private';
    static $const = 'const';
    static $enddeclare = 'enddeclare';
    static $endfor = 'endfor';
    static $endforeach = 'endforeach';
    static $endif = 'endif';
    static $endwhile = 'endwhile';
    static $and = 'and';
    static $global = 'global';
    static $goto = 'goto';
    static $instanceof = 'instanceof';
    static $insteadof = 'insteadof';
    static $interface = 'interface';
    static $namespace = 'namespace';
    static $new = 'new';
    static $or = 'or';
    static $xor = 'xor';
    static $try = 'try';
    static $use = 'use';
    static $var = 'var';
    static $exit = 'exit';
    static $list = 'list';
    static $clone = 'clone';
    static $include = 'include';
    static $include_once = 'include_once';
    static $throw = 'throw';
    static $array = 'array';
    static $print = 'print';
    static $echo = 'echo';
    static $require = 'require';
    static $require_once = 'require_once';
    static $return = 'return';
    static $else = 'else';
    static $elseif = 'elseif';
    static $default = 'default';
    static $break = 'break';
    static $continue = 'continue';
    static $switch = 'switch';
    static $yield = 'yield';
    static $function = 'function';
    static $if = 'if';
    static $endswitch = 'endswitch';
    static $finally = 'finally';
    static $for = 'for';
    static $foreach = 'foreach';
    static $declare = 'declare';
    static $case = 'case';
    static $do = 'do';
    static $while = 'while';
    static $as = 'as';
    static $catch = 'catch';
    static $die = 'die';
    static $self = 'self';
    static $parent = 'parent';
}

echo Obj::$empty, "\n";
echo Obj::$callable, "\n";
echo Obj::$class, "\n";
echo Obj::$trait, "\n";
echo Obj::$extends, "\n";
echo Obj::$implements, "\n";
echo Obj::$static, "\n";
echo Obj::$abstract, "\n";
echo Obj::$final, "\n";
echo Obj::$public, "\n";
echo Obj::$protected, "\n";
echo Obj::$private, "\n";
echo Obj::$const, "\n";
echo Obj::$enddeclare, "\n";
echo Obj::$endfor, "\n";
echo Obj::$endforeach, "\n";
echo Obj::$endif, "\n";
echo Obj::$endwhile, "\n";
echo Obj::$and, "\n";
echo Obj::$global, "\n";
echo Obj::$goto, "\n";
echo Obj::$instanceof, "\n";
echo Obj::$insteadof, "\n";
echo Obj::$interface, "\n";
echo Obj::$namespace, "\n";
echo Obj::$new, "\n";
echo Obj::$or, "\n";
echo Obj::$xor, "\n";
echo Obj::$try, "\n";
echo Obj::$use, "\n";
echo Obj::$var, "\n";
echo Obj::$exit, "\n";
echo Obj::$list, "\n";
echo Obj::$clone, "\n";
echo Obj::$include, "\n";
echo Obj::$include_once, "\n";
echo Obj::$throw, "\n";
echo Obj::$array, "\n";
echo Obj::$print, "\n";
echo Obj::$echo, "\n";
echo Obj::$require, "\n";
echo Obj::$require_once, "\n";
echo Obj::$return, "\n";
echo Obj::$else, "\n";
echo Obj::$elseif, "\n";
echo Obj::$default, "\n";
echo Obj::$break, "\n";
echo Obj::$continue, "\n";
echo Obj::$switch, "\n";
echo Obj::$yield, "\n";
echo Obj::$function, "\n";
echo Obj::$if, "\n";
echo Obj::$endswitch, "\n";
echo Obj::$finally, "\n";
echo Obj::$for, "\n";
echo Obj::$foreach, "\n";
echo Obj::$declare, "\n";
echo Obj::$case, "\n";
echo Obj::$do, "\n";
echo Obj::$while, "\n";
echo Obj::$as, "\n";
echo Obj::$catch, "\n";
echo Obj::$die, "\n";
echo Obj::$self, "\n";
echo Obj::$parent, "\n";

echo "\nDone\n";

?>
--EXPECTF--
empty
callable
class
trait
extends
implements
static
abstract
final
public
protected
private
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