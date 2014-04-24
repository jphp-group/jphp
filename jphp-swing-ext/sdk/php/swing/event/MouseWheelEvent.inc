<?php
namespace php\swing\event;

/**
 * Events:
 *
 *      * mouseWheel
 *
 * Class MouseWheelEvent
 * @package php\swing\event
 */
class MouseWheelEvent extends MouseEvent {
    /**
     * @readonly
     * @var int
     */
    public $scrollAmount;

    /**
     * @readonly
     * @var int
     */
    public $scrollType;

    /**
     * @readonly
     * @var int
     */
    public $wheelRotation;

    /**
     * @readonly
     * @var int
     */
    public $unitsToScroll;
}
