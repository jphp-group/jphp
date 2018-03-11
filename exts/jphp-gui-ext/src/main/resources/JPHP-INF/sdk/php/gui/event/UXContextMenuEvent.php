<?php
namespace php\gui\event;

/**
 * Class UXContextMenuEvent
 * @package php\gui\event
 * @packages gui, javafx
 */
class UXContextMenuEvent extends UXEvent
{
    /**
     * @readonly
     * @var double
     */
    public $sceneX;

    /**
     * @readonly
     * @var double
     */
    public $sceneY;

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
     * @var double
     */
    public $x;

    /**
     * @readonly
     * @var double
     */
    public $y;

    /**
     * @readonly
     * @var bool
     */
    public $keyboardTrigger;
}