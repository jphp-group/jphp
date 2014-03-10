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

return 'success';
