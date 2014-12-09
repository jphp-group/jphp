<?php
namespace php\swing\event;

/**
 * Events:
 *
 *      * keyUp
 *      * keyDown
 *      * keyPress
 *
 * Class KeyEvent
 * @package php\swing\event
 */
class KeyEvent extends ComponentEvent {
    /**
     * @readonly
     * @var string
     */
    public $keyChar;

    /**
     * @readonly
     * @var int
     */
    public $keyCode;

    /**
     * @readonly
     * @var int
     */
    public $keyLocation;

    /**
     * @readonly
     * @var bool
     */
    public $actionKey;
}
