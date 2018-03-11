<?php
namespace php\gui;

/**
 * Class UXLabel
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXLabel extends UXLabeled
{


    /**
     * @var UXNode
     */
    public $labelFor;

    /**
     * @param string $text (optional)
     * @param UXNode $graphic (optional)
     */
    public function __construct($text = '', UXNode $graphic = null) {}
}