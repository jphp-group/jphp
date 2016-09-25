--FILE--
<?php
namespace {
    error_reporting(E_ALL);
    define('CONST_1', 1);
}

namespace app\forms{
    define('CONST_2', 2);

    class MainForm
    {

        /**
         * @event show
         */
        function doShow()
        {
            var_dump(CONST_1, CONST_2, CONST_3);
            var_dump(\CONST_1, \CONST_2);
            var_dump(get_defined_constants(true)['user']);
        }
    }

    $form = new MainForm();
    $form->doShow();
}
?>
--EXPECTF--
Notice: Use of undefined constant CONST_3 - assumed 'CONST_3' in %s on line 18 at pos 40
int(1)
int(2)
string(7) "CONST_3"
int(1)
int(2)
array(2) {
  ["CONST_1"]=>
  int(1)
  ["CONST_2"]=>
  int(2)
}
