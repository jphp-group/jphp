--TEST--
Issue #110
https://github.com/jphp-compiler/jphp/issues/110
--FILE--
<?php
function t(){
    ob_implicit_flush(true);

    $pasv = false;

    switch ('14123423') {
        case 'sdfgsdfg':
            $pasv = true;
            break;

        default:
            //var_dump($pasv); //If uncommented, is working correctly
            echo (int)$pasv, "\r\n";
    }
}
t();
?>
--EXPECT--
0
