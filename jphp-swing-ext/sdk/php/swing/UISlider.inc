<?php
namespace php\swing;

/**
 * Class UISlider
 * @package php\swing
 *
 *      events: change
 */
class UISlider extends UIContainer {
    /**
     * @var int
     */
    public $value;

    /**
     * @var int
     */
    public $min;

    /**
     * @var int
     */
    public $max;

    /**
     * @var int
     */
    public $extent;

    /**
     * @var bool
     */
    public $valueIsAdjusting;

    /**
     * @var bool
     */
    public $inverted;

    /**
     * @var bool
     */
    public $paintLabels;

    /**
     * @var bool
     */
    public $paintTicks;

    /**
     * @var bool
     */
    public $paintTrack;

    /**
     * @var bool
     */
    public $snapToTicks;

    /**
     * @var string[]
     */
    public $labelTable;

    /**
     * @var int
     */
    public $majorTickSpacing;

    /**
     * @var int
     */
    public $minorTickSpacing;
}
