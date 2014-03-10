<?php
namespace my;

use my\Foobar as Foobar2;

class Foobar {
    static function test(){
        return 'success';
    }
}

if (Foobar2::test() !== 'success')
    return 'fail_1';

if (Foobar::test() !== 'success')
    return 'fail_2';

if (\my\Foobar::test() !== 'success')
    return 'fail_3';

return 'success';