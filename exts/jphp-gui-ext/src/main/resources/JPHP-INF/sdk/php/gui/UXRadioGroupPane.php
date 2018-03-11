<?php
namespace php\gui;

use php\gui\layout\UXVBox;
use php\gui\paint\UXColor;
use php\gui\text\UXFont;

/**
 * Class UXRadioGroupPane
 * @package php\gui
 * @packages gui, javafx
 */
class UXRadioGroupPane extends UXVBox
{


    /**
     * @var UXList
     */
    public $items;

    /**
     * @var mixed
     */
    public $selected;

    /**
     * @var int
     */
    public $selectedIndex = -1;

    /**
     * @var string
     */
    public $orientation = 'VERTICAL';

    /**
     * @var UXFont
     */
    public $font;

    /**
     * @var UXColor
     */
    public $textColor;

    /**
     * Update ui.
     */
    public function update()
    {
    }
}