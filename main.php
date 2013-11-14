<?

class MyClass {

    public static function test() {

        for($i=0; $i < 30000000; $i++){
            switch(true){
                case $i == 1: $x = 20; break;
                case $i == 2: $x = 30; break;
                case $i == 3: $x = 40; break;
                default:
                    $x = 50;
            }
        }
        echo($x);
    }
}
