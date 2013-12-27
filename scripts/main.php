<?php


class A { }
interface IA { }
class C extends A implements IA { }

$c = new C();
if (!($c instanceof iA))
    return 'fail_ia';