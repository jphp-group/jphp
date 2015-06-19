--TEST--
Test semi-reserved words as class properties
--FILE--
<?php

class Obj
{
    var $empty = 'empty';
    var $callable = 'callable';
    var $class = 'class';
    var $trait = 'trait';
    var $extends = 'extends';
    var $implements = 'implements';
    var $static = 'static';
    var $abstract = 'abstract';
    var $final = 'final';
    var $public = 'public';
    var $protected = 'protected';
    var $private = 'private';
    var $const = 'const';
    var $enddeclare = 'enddeclare';
    var $endfor = 'endfor';
    var $endforeach = 'endforeach';
    var $endif = 'endif';
    var $endwhile = 'endwhile';
    var $and = 'and';
    var $global = 'global';
    var $goto = 'goto';
    var $instanceof = 'instanceof';
    var $insteadof = 'insteadof';
    var $interface = 'interface';
    var $namespace = 'namespace';
    var $new = 'new';
    var $or = 'or';
    var $xor = 'xor';
    var $try = 'try';
    var $use = 'use';
    var $var = 'var';
    var $exit = 'exit';
    var $list = 'list';
    var $clone = 'clone';
    var $include = 'include';
    var $include_once = 'include_once';
    var $throw = 'throw';
    var $array = 'array';
    var $print = 'print';
    var $echo = 'echo';
    var $require = 'require';
    var $require_once = 'require_once';
    var $return = 'return';
    var $else = 'else';
    var $elseif = 'elseif';
    var $default = 'default';
    var $break = 'break';
    var $continue = 'continue';
    var $switch = 'switch';
    var $yield = 'yield';
    var $function = 'function';
    var $if = 'if';
    var $endswitch = 'endswitch';
    var $finally = 'finally';
    var $for = 'for';
    var $foreach = 'foreach';
    var $declare = 'declare';
    var $case = 'case';
    var $do = 'do';
    var $while = 'while';
    var $as = 'as';
    var $catch = 'catch';
    var $die = 'die';
    var $self = 'self';
}

$obj = new Obj;

echo $obj->empty, "\n";
echo $obj->callable, "\n";
echo $obj->class, "\n";
echo $obj->trait, "\n";
echo $obj->extends, "\n";
echo $obj->implements, "\n";
echo $obj->static, "\n";
echo $obj->abstract, "\n";
echo $obj->final, "\n";
echo $obj->public, "\n";
echo $obj->protected, "\n";
echo $obj->private, "\n";
echo $obj->const, "\n";
echo $obj->enddeclare, "\n";
echo $obj->endfor, "\n";
echo $obj->endforeach, "\n";
echo $obj->endif, "\n";
echo $obj->endwhile, "\n";
echo $obj->and, "\n";
echo $obj->global, "\n";
echo $obj->goto, "\n";
echo $obj->instanceof, "\n";
echo $obj->insteadof, "\n";
echo $obj->interface, "\n";
echo $obj->namespace, "\n";
echo $obj->new, "\n";
echo $obj->or, "\n";
echo $obj->xor, "\n";
echo $obj->try, "\n";
echo $obj->use, "\n";
echo $obj->var, "\n";
echo $obj->exit, "\n";
echo $obj->list, "\n";
echo $obj->clone, "\n";
echo $obj->include, "\n";
echo $obj->include_once, "\n";
echo $obj->throw, "\n";
echo $obj->array, "\n";
echo $obj->print, "\n";
echo $obj->echo, "\n";
echo $obj->require, "\n";
echo $obj->require_once, "\n";
echo $obj->return, "\n";
echo $obj->else, "\n";
echo $obj->elseif, "\n";
echo $obj->default, "\n";
echo $obj->break, "\n";
echo $obj->continue, "\n";
echo $obj->switch, "\n";
echo $obj->yield, "\n";
echo $obj->function, "\n";
echo $obj->if, "\n";
echo $obj->endswitch, "\n";
echo $obj->finally, "\n";
echo $obj->for, "\n";
echo $obj->foreach, "\n";
echo $obj->declare, "\n";
echo $obj->case, "\n";
echo $obj->do, "\n";
echo $obj->while, "\n";
echo $obj->as, "\n";
echo $obj->catch, "\n";
echo $obj->die, "\n";
echo $obj->self, "\n";

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

Done
