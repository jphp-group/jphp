<?

$_POST = array(1, 2, 3, 4);

function test($x, $y){
    if ($x){
        if ($y){
            print_r($_POST);
        }
    }
    print_r($_POST);
}

test(true, true);