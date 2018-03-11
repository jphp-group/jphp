<?php
namespace php\gui;

use php\gui\paint\UXColor;

/**
 * Class UXMaterialButton
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXMaterialButton extends UXButton
{
    /**
     * FLAT, RAISED
     * @var string
     */
    public $buttonType = 'FLAT';

    /**
     * @var UXColor
     */
    public $ripplerFill;
}