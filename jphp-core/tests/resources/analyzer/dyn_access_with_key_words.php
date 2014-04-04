--TEST--
Dynamic access with using key words
--FILE--
<?php
error_reporting(0);

$x = new stdClass();

$x->class;
unset($x->class);
$a = isset($x->class);
$a = empty($x->class);
$x->class = 'foobar';

$x->protected;
$x->private;
$x->public;
$x->static;
$x->final;
$x->parent;
$x->self;
$x->static;
$x->function;
$x->use;
$x->array;
$x->new;
$x->clone;
$x->instanceof;
$x->insteadof;
$x->true;
$x->false;
$x->null;
$x->as;
$x->if;
$x->else;
$x->elseif;
$x->while;
$x->do;
$x->foreach;
$x->for;
$x->namespace;
$x->use;
$x->interface;
$x->trait;
$x->switch;
$x->case;
$x->return;
$x->endif;
$x->endfor;
$x->endforeach;
$x->endwhile;
$x->endswitch;
$x->enddeclare;
$x->break;
$x->continue;
$x->goto;
$x->isset;
$x->unset;
$x->empty;
$x->die;
$x->exit;
$x->abstract;
$x->var;
$x->try;
$x->catch;
$x->finally;
$x->throw;
$x->extends;
$x->implements;
$x->global;
$x->list;
$x->__line__;
$x->__file__;
$x->__dir__;
$x->__function__;
$x->__class__;
$x->__method__;
$x->__trait__;
$x->__namespace__;
$x->include;
$x->include_once;
$x->require;
$x->require_once;
$x->echo;
$x->print;

echo "success";

?>
--EXPECTF--
success
