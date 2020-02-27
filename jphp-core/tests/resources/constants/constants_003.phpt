--TEST--
Test accessing to array class constant
--FILE--
<?
class a
{
    const a = ['a'];
    private const b = ['b'];
    public function __construct()
    {
        self::a[0] = 20;
    }
}
new a;
--EXPECTF--

Parse error: Syntax error, unexpected '=' in %s on line 8, position %d