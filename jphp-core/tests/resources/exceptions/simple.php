<?php

try {
    throw new Exception;
} catch (Exception $e){
    return 'success';
}

return 'fail';
