<?php
namespace php\gui;

use php\gui\paint\UXColor;
use php\time\Time;

/**
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXMaterialTimePicker extends UXComboBoxBase
{
    /**
     * @var UXColor
     */
    public $defaultColor;

    /**
     * @var bool
     */
    public $overlay;

    /**
     * @var bool
     */
    public $hourView24;

    /**
     * @var Time
     */
    public $valueAsTime;

    /**
     * @var string
     */
    public $format = 'HH:mm';
}