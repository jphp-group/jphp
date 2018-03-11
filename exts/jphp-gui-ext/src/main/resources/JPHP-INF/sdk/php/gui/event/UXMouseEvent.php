<?php
namespace php\gui\event;

/**
 * Class UXMouseEvent
 * @package php\gui\event
 * @packages gui, javafx
 */
class UXMouseEvent extends UXEvent
{
    /**
     * @readonly
     * @var double
     */
    public $x;

    /**
     * @readonly
     * @var double
     */
    public $y;

    /**
     * [x, y] position
     *
     * @readonly
     * @var array
     */
    public $position = [0.0, 0.0];

    /**
     * @readonly
     * @var double
     */
    public $screenX;

    /**
     * @readonly
     * @var double
     */
    public $screenY;

    /**
     * @readonly
     * @var string
     */
    public $button;

    /**
     * @readonly
     * @var int
     */
    public $clickCount;

    /**
     * @var bool
     */
    public $altDown;

    /**
     * @var bool
     */
    public $controlDown;

    /**
     * @var bool
     */
    public $shiftDown;

    /**
     * @var bool
     */
    public $metaDown;

    /**
     * @var bool
     */
    public $shortcutDown;

    /**
     * Returns true if clickCount >= 2.
     * @return bool
     */
    public function isDoubleClick()
    {
    }
}