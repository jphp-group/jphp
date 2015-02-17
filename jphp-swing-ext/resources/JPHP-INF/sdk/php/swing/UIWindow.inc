<?php
namespace php\swing;

abstract class UIWindow extends UIContainer {
    /**
     * @var string
     */
    public $title;

    /**
     * @var float - 0 .. 1
     */
    public $opacity;

    /**
     * @var bool
     */
    public $alwaysOnTop;

    /**
     * @var bool
     */
    public $resizable;

    /**
     * @var bool
     */
    public $undecorated;

    /**
     * @return bool
     */
    public function isActive() { }

    /**
     * @return bool
     */
    public function isAlwaysOnTopSupported() { }

    /**
     * Move window to screen center
     */
    public function moveToCenter() { }
}
