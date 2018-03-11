<?php
namespace php\gui;

/**
 * Class UXChoiceBox
 * @package php\gui
 * @packages gui, javafx
 */
class UXChoiceBox extends UXControl
{


    /**
     * @var UXList
     */
    public $items;

    /**
     * @var mixed
     */
    public $value = null;

    /**
     * @var int
     */
    public $selectedIndex = -1;

    /**
     * ...
     */
    public function update()
    {
    }
}