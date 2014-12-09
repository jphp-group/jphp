<?php
namespace php\swing\event;

/**
 * Events:
 *
 *      * click
 *      * mousePress
 *      * mouseRelease
 *      * mouseEnter
 *      * mouseExit
 *      * mouseDrag
 *      * mouseMove
 *
 * Class MouseEvent
 * @package php\swing\event
 */
class MouseEvent extends ComponentEvent {
    /**
     * @readonly
     * @var int
     */
    public $x;

    /**
     * @readonly
     * @var int
     */
    public $y;

    /**
     * @readonly
     * @var int
     */
    public $screenX;

    /**
     * @readonly
     * @var int
     */
    public $screenY;

    /**
     * @readonly
     * @var int
     */
    public $button;

    /**
     * @readonly
     * @var int
     */
    public $clickCount;

    /**
     * @readonly
     * @var bool
     */
    public $popupTrigger;
}
