<?php
namespace php\gui;

/**
 * Class UXSlider
 * @package php\gui
 * @packages gui, javafx
 */
class UXSlider extends UXControl
{
    /**
     * @var double
     */
    public $min;

    /**
     * @var double
     */
    public $max;

    /**
     * @var double
     */
    public $value;

    /**
     * @var double
     */
    public $blockIncrement;

    /**
     * @var bool
     */
    public $showTickMarks;

    /**
     * @var bool
     */
    public $showTickLabels;

    /**
     * @var bool
     */
    public $snapToTicks;

    /**
     * @var double
     */
    public $majorTickUnit = 25;

    /**
     * @var int
     */
    public $minorTickUnit = 0;

    /**
     * @var string
     */
    public $orientation = 'HORIZONTAL';
}