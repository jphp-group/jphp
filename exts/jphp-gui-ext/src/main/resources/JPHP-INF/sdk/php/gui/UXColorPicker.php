<?php
namespace php\gui;
use php\gui\paint\UXColor;

/**
 * Class UXColorPicker
 * @package php\gui
 * @packages gui, javafx
 */
class UXColorPicker extends UXComboBoxBase
{


    /**
     * @var UXList of UXColor
     */
    public $customColors;

    /**
     * @var UXColor
     */
    public $value;

    /**
     * @param UXColor $color [optional]
     */
    public function __construct(UXColor $color) {}
}