<?php

$e -= ($b1[6]*$b2[6])/sqrt($dx*$dx + $dy*$dy + $dz*$dz);

return $e == 0 ? 'success' : 'fail';
