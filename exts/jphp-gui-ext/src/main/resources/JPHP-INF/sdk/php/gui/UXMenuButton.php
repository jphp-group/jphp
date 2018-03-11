<?php
namespace php\gui;

/**
 * Class UXMenuButton
 * @package php\gui
 * @packages gui, javafx
 */
class UXMenuButton extends UXButtonBase
{


    /**
     * List of UXMenuItem
     * @var UXList
     */
    public $items;

    /**
     * BOTTOM, TOP, LEFT or RIGHT.
     * @var string
     */
    public $popupSide = 'BOTTOM';

    /**
     * UXMenuButton constructor.
     * @param string $text
     * @param UXNode|null $graphic
     */
    public function __construct($text = null, UXNode $graphic = null)
    {
    }

    /**
     * Show popup menu.
     */
    public function showMenu()
    {
    }

    /**
     * Hide popup menu.
     */
    public function hideMenu()
    {
    }
}