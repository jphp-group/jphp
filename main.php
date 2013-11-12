<?

class MyClass {

    public static function func($x){
        return $x + 1;
    }

    public static function test() {
        $func = 'func';
        while($i++ < 4000000){
            $x = self::func($i);
        }
    }
}
