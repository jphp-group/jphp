<?php
namespace foo\bar;

use foo\bar\Foobar as My, foo\bar\Foobar as Bar, foo;


class Foobar {
    static function test(){
        return 'success';
    }
}

if (My::test() !== 'success')
    return 'fail_1';

if (Bar::test() !== 'success')
    return 'fail_2';

return 'success';