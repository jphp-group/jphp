<?php
namespace php\gui;

/**
 * Class UXCheckbox
 * @package php\gui
 * @packages gui, javafx
 */
class UXCheckbox extends UXButtonBase
{


    /**
     * Отмечен или нет.
     * @var bool
     */
    public $selected;

    /**
     * @var bool
     */
    public $indeterminate;

    /**
     * @var bool
     */
    public $allowIndeterminate;

    /**
     * @param string $text [optional]
     */
    public function __construct($text) {}
}