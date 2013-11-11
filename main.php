<?

class MyClass {

    public static function func($x, $y = 30, $z = 'abcd'){
        return $x + 1;
    }

    public static function test() {

        while($i++ < 1000000){
            $x += MyClass::func($i);
            $x += MyClass::func($i);
            $x += MyClass::func($i);
            $x += MyClass::func($i);
            $x += MyClass::func($i);
        }
    }
}
