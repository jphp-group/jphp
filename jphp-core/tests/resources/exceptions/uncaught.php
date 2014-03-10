<?php

class X extends Exception { }
class Y extends Exception { }

try {
   throw new X;
} catch (Y $e){
    return 'fail';
}