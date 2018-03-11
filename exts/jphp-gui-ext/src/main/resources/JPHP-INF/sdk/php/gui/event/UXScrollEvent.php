<?php
namespace php\gui\event;

/**
 * Class UXScrollEvent
 * @package php\gui\event
 * @packages gui, javafx
 */
class UXScrollEvent extends UXEvent
{
    /**
     * @readonly
     * @var double
     */
    public $deltaX, $deltaY;

    /**
     * @readonly
     * @var double
     */
    public $textDeltaX, $textDeltaY;

    /**
     * @readonly
     * @var double
     */
    public $totalDeltaX, $totalDeltaY;

    /**
     * @readonly
     * @var double
     */
    public $multiplierX, $multiplierY;

    /**
     * @readonly
     * @var int
     */
    public $touchCount;

    /**
     * @readonly
     * @var string SCROLL, SCROLL_STARTED, SCROLL_FINISHED
     */
    public $eventType;

    /**
     * Returns information about the pick.
     *
     * @readonly
     * @var array
     */
    public $pickResult = [];

    /**
     * Indicates whether or not the Alt modifier is down on this event.
     *
     * @readonly
     * @var bool
     */
    public $altDown = false;

    /**
     * Indicates whether or not the Shift modifier is down on this event.
     *
     * @readonly
     * @var bool
     */
    public $shiftDown = false;

    /**
     * Indicates whether or not the Control modifier is down on this event.
     *
     * @readonly
     * @var bool
     */
    public $controlDown = false;

    /**
     * Indicates whether or not the Meta modifier is down on this event.
     *
     * @readonly
     * @var bool
     */
    public $metaDown = false;

    /**
     * Indicates whether or not the Shortcut modifier is down on this event.
     *
     * @readonly
     * @var bool
     */
    public $shortcutDown = false;

    /**
     * Indicates whether this gesture is caused by a direct or indirect input
     * device. With direct input device the gestures are performed directly at
     * the concrete coordinates, a typical example would be a touch screen.
     * With indirect device the gestures are performed indirectly and usually
     * mouse cursor position is used as the gesture coordinates, a typical
     * example would be a track pad.
     *
     * @return bool
     */
    public function isDirect()
    {
    }

    /**
     * Indicates if this event represents an inertia of an already finished
     * gesture.
     *
     * @return bool
     */
    public function isInertia()
    {
    }
}