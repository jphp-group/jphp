<?php
namespace php\gui;

use php\gui\paint\UXColor;

/**
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXMaterialToggleButton extends UXToggleButton
{
    /**
     * @var UXColor
     */
    public $toggleColor;

    /**
     * @var UXColor
     */
    public $toggleLineColor;

    /**
     * @var UXColor
     */
    public $unToggleColor;

    /**
     * @var UXColor
     */
    public $unToggleLineColor;
}