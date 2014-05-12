<?php

class A { }
class B extends A { }
interface IA { }
interface IB { }
interface IC extends IA, IB { }

class C extends B implements IC, iA { }

$c = new C();
if (!($c instanceof iA && $c instanceof Ib && $c instanceof IC))
    return 'fail_ia_ib_ic';

if (!($c instanceof A && $c instanceof b))
    return 'fail_a_b';


/** #105 @link https://github.com/jphp-compiler/jphp/issues/105 */
class Foo {
    public $type = '\\Foo';
    public $type2 = 'Foo';
}

$a = new Foo();
if (!($a instanceof $a->type))
    return 'fail_3';

if (!($a instanceof $a->type2))
    return 'fail_4';

$arr['x']['y'] = 'Foo';
if (!($a instanceof $arr['x']['y']))
    return 'fail_5: instanceof with array access';

return 'success';
