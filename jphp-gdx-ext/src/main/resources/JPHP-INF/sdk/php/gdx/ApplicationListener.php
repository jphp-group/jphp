<?php
namespace php\gdx;

interface ApplicationListener {
    function create();
    function resize($width, $height);
    function render();
    function pause();
    function resume();
    function dispose();
}
