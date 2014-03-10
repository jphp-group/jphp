<?php

// see: http://www.php.net/manual/en/types.comparisons.php

$x = "";
if (gettype($x) !== "string") return "fail_str_1";
if (!empty($x)) return "fail_str_2";
if (is_null($x)) return "fail_str_3";
if (!isset($x)) return "fail_str_4";
if ((bool)$x) return "fail_str_5";

$x = NULL;
if (gettype($x) !== 'NULL') return 'fail_null_1';
if (!empty($x)) return 'fail_null_2';
if (!is_null($x)) return 'fail_null_3';
if (isset($x)) return 'fail_null_4';
if ((bool)$x) return 'fail_null_5';

if (gettype($y) !== 'NULL') return 'fail_undef_1';
if (!empty($y)) return 'fail_undef_2';
if (!is_null($y)) return 'fail_undef_3';
if (isset($y)) return 'fail_undef_4';
if ((bool)$y) return 'fail_undef_5';

$x = array();
if (gettype($x) !== 'array') return 'fail_array_1';
if (!empty($x)) return 'fail_array_2';
if (is_null($x)) return 'fail_array_3';
if (!isset($x)) return 'fail_array_4';
if ((bool)$x) return 'fail_array_5';

$x = false;
if (gettype($x) !== 'boolean') return 'fail_false_1';
if (!empty($x)) return 'fail_false_2';
if (is_null($x)) return 'fail_false_3';
if (!isset($x)) return 'fail_false_4';
if ((bool)$x) return 'fail_false_5';

$x = true;
if (gettype($x) !== 'boolean') return 'fail_true_1';
if (empty($x)) return 'fail_true_2';
if (is_null($x)) return 'fail_true_3';
if (!isset($x)) return 'fail_true_4';
if (!(bool)$x) return 'fail_true_5';

$x = 1;
if (gettype($x) !== 'integer') return 'fail_1_1';
if (empty($x)) return 'fail_1_2';
if (is_null($x)) return 'fail_1_3';
if (!isset($x)) return 'fail_1_4';
if (!(bool)$x) return 'fail_1_5';

$x = 0;
if (gettype($x) !== 'integer') return 'fail_0_1';
if (!empty($x)) return 'fail_0_2';
if (is_null($x)) return 'fail_0_3';
if (!isset($x)) return 'fail_0_4';
if ((bool)$x) return 'fail_0_5';

$x = "-1";
if (gettype($x) !== 'string') return 'fail_"-1"_1';
if (empty($x)) return 'fail_"-1"_2';
if (is_null($x)) return 'fail_"-1"_3';
if (!isset($x)) return 'fail_"-1"_4';
if (!(bool)$x) return 'fail_"-1"_5';

return 'success';