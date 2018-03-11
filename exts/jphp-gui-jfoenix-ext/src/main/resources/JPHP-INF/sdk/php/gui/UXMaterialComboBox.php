<?php
namespace php\gui;

use php\gui\paint\UXColor;

/**
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXMaterialComboBox extends UXComboBox
{
    /**
     * @var bool
     */
    public $labelFloat;

    /**
     * @var UXColor
     */
    public $focusColor;

    /**
     * @var UXColor
     */
    public $unfocusColor;
}