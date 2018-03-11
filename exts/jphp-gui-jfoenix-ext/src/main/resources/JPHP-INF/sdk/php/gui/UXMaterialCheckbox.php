<?php
namespace php\gui;

use php\gui\paint\UXColor;

/**
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXMaterialCheckbox extends UXCheckbox
{
    /**
     * @var UXColor
     */
    public $checkedColor;

    /**
     * @var UXColor
     */
    public $uncheckedColor;
}