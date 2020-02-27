--TEST--
Test accessing to array class constant
--FILE--
<?
class a
{
    const a = ['a'];
    private const b = ['b'];
    const c = 'c';
    const d = 'd';
    public function __construct()
    {
        var_dump(self::a[0]);
        var_dump(self::b[0]);
        var_dump(self::c[0]);
        var_dump(self::d[0]);
    }
}
new a;
--EXPECTF--
string(1) "a"
string(1) "b"
string(1) "c"
string(1) "d"