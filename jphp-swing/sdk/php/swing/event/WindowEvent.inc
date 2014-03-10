<?php
namespace php\swing\event;

use php\swing\UIWindow;

/**
 * Events:
 *      - windowOpen
 *      - windowClose
 *      - windowClosing
 *      - windowActive
 *
 * Class WindowEvent
 * @package php\swing\event
 */
class WindowEvent extends ComponentEvent {
    /**
     * @readonly
     * @var int
     */
    public $oldState;

    /**
     * @readonly
     * @var int
     */
    public $newState;

    /**
     * @readonly
     * @var UIWindow
     */
    public $oppositeWindow;
}
