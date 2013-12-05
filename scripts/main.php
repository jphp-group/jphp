<?

function test(){
    $fruit = array('a' => 'apple', 'b' => 'banana', 'c' => 'cranberry');

    for($i = 0; $i < 1000000; $i++){
        reset($fruit);
        while ($item = each($fruit)) {

        }
    }
}

test();