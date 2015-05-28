--TEST--
Test semi-reserved words as static class methods
--FILE--
<?php

class Obj
{
    static function empty(){ echo __METHOD__, "\n"; }
static function callable(){ echo __METHOD__, "\n"; }
    static function trait(){ echo __METHOD__, "\n"; }
    static function extends(){ echo __METHOD__, "\n"; }
    static function implements(){ echo __METHOD__, "\n"; }
    static function const(){ echo __METHOD__, "\n"; }
    static function enddeclare(){ echo __METHOD__, "\n"; }
    static function endfor(){ echo __METHOD__, "\n"; }
    static function endforeach(){ echo __METHOD__, "\n"; }
    static function endif(){ echo __METHOD__, "\n"; }
    static function endwhile(){ echo __METHOD__, "\n"; }
    static function and(){ echo __METHOD__, "\n"; }
    static function global(){ echo __METHOD__, "\n"; }
    static function goto(){ echo __METHOD__, "\n"; }
    static function instanceof(){ echo __METHOD__, "\n"; }
    static function insteadof(){ echo __METHOD__, "\n"; }
    static function interface(){ echo __METHOD__, "\n"; }
    static function new(){ echo __METHOD__, "\n"; }
    static function or(){ echo __METHOD__, "\n"; }
    static function xor(){ echo __METHOD__, "\n"; }
    static function try(){ echo __METHOD__, "\n"; }
    static function use(){ echo __METHOD__, "\n"; }
    static function var(){ echo __METHOD__, "\n"; }
    static function exit(){ echo __METHOD__, "\n"; }
    static function list(){ echo __METHOD__, "\n"; }
    static function clone(){ echo __METHOD__, "\n"; }
    static function include(){ echo __METHOD__, "\n"; }
    static function include_once(){ echo __METHOD__, "\n"; }
    static function throw(){ echo __METHOD__, "\n"; }
    static function array(){ echo __METHOD__, "\n"; }
    static function print(){ echo __METHOD__, "\n"; }
    static function echo(){ echo __METHOD__, "\n"; }
    static function require(){ echo __METHOD__, "\n"; }
    static function require_once(){ echo __METHOD__, "\n"; }
    static function return(){ echo __METHOD__, "\n"; }
    static function else(){ echo __METHOD__, "\n"; }
    static function elseif(){ echo __METHOD__, "\n"; }
    static function default(){ echo __METHOD__, "\n"; }
    static function break(){ echo __METHOD__, "\n"; }
    static function continue(){ echo __METHOD__, "\n"; }
    static function switch(){ echo __METHOD__, "\n"; }
    static function yield(){ echo __METHOD__, "\n"; }
    static function function(){ echo __METHOD__, "\n"; }
    static function if(){ echo __METHOD__, "\n"; }
    static function endswitch(){ echo __METHOD__, "\n"; }
    static function finally(){ echo __METHOD__, "\n"; }
    static function for(){ echo __METHOD__, "\n"; }
    static function foreach(){ echo __METHOD__, "\n"; }
    static function declare(){ echo __METHOD__, "\n"; }
    static function case(){ echo __METHOD__, "\n"; }
    static function do(){ echo __METHOD__, "\n"; }
    static function while(){ echo __METHOD__, "\n"; }
    static function as(){ echo __METHOD__, "\n"; }
    static function catch(){ echo __METHOD__, "\n"; }
    static function die(){ echo __METHOD__, "\n"; }
    static function self(){ echo __METHOD__, "\n"; }
    static function parent(){ echo __METHOD__, "\n"; }
}

Obj::empty();
Obj::callable();
Obj::trait();
Obj::extends();
Obj::implements();
Obj::const();
Obj::enddeclare();
Obj::endfor();
Obj::endforeach();
Obj::endif();
Obj::endwhile();
Obj::and();
Obj::global();
Obj::goto();
Obj::instanceof();
Obj::insteadof();
Obj::interface();
Obj::new();
Obj::or();
Obj::xor();
Obj::try();
Obj::use();
Obj::var();
Obj::exit();
Obj::list();
Obj::clone();
Obj::include();
Obj::include_once();
Obj::throw();
Obj::array();
Obj::print();
Obj::echo();
Obj::require();
Obj::require_once();
Obj::return();
Obj::else();
Obj::elseif();
Obj::default();
Obj::break();
Obj::continue();
Obj::switch();
Obj::yield();
Obj::function();
Obj::if();
Obj::endswitch();
Obj::finally();
Obj::for();
Obj::foreach();
Obj::declare();
Obj::case();
Obj::do();
Obj::while();
Obj::as();
Obj::catch();
Obj::die();
Obj::self();
Obj::parent();

echo "\nDone\n";

?>
--EXPECTF--
Obj::empty
Obj::callable
Obj::trait
Obj::extends
Obj::implements
Obj::const
Obj::enddeclare
Obj::endfor
Obj::endforeach
Obj::endif
Obj::endwhile
Obj::and
Obj::global
Obj::goto
Obj::instanceof
Obj::insteadof
Obj::interface
Obj::new
Obj::or
Obj::xor
Obj::try
Obj::use
Obj::var
Obj::exit
Obj::list
Obj::clone
Obj::include
Obj::include_once
Obj::throw
Obj::array
Obj::print
Obj::echo
Obj::require
Obj::require_once
Obj::return
Obj::else
Obj::elseif
Obj::default
Obj::break
Obj::continue
Obj::switch
Obj::yield
Obj::function
Obj::if
Obj::endswitch
Obj::finally
Obj::for
Obj::foreach
Obj::declare
Obj::case
Obj::do
Obj::while
Obj::as
Obj::catch
Obj::die
Obj::self
Obj::parent

Done