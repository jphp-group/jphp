<?php
namespace php\gui;

/**
 * Class UXToggleButton
 * @package php\gui
 * @packages gui, javafx
 */
class UXToggleButton extends UXButtonBase
{
    /**
     * @var bool
     */
    public $selected = false;

    /**
     * @var UXToggleGroup
     */
    public $toggleGroup = null;
}