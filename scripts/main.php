<?php


class X extends Exception {}

try {
    throw new X;
} catch (Exception $e){
    echo "aaa";
}