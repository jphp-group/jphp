<?php
namespace php\gui;

/**
 * Class ProgressIndicator
 * @package php\gui
 * @packages gui, javafx
 */
class UXProgressIndicator extends UXControl
{


    /**
     * @var double
     */
    public $progressK = 0.0;

    /**
     * @var int
     */
    public $progress = 0;

    /**
     * @readonly
     * @var bool
     */
    public $indeterminate;
}