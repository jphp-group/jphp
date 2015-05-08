<?php
namespace ext\javafx;
use ext\javafx\paint\UXColor;

/**
 * Class UXColorPicker
 * @package ext\javafx
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
     * @param UXColor $color (optional)
     */
    public function __construct(UXColor $color) {}
}