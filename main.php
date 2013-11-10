<?

class MyClass {

    public static function test() {

        $i = 0;
        while(($i+=1) < 10000000){
            $x = 20;
        }

        echo($x);
    }
}
