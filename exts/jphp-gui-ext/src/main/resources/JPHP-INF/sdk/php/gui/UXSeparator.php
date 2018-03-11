<?php
namespace php\gui;

/**
 * Class UXSeparator
 * @package php\gui
 * @packages gui, javafx
 */
class UXSeparator extends UXControl
{
    /**
     * @var string HORIZONTAL or VERTICAL
     */
    public $orientation = 'HORIZONTAL';

    /**
     * @var string
     */
    public $hAlignment;

    /**
     * @var string
     */
    public $vAlignment;

    /**
     * UXSeparator constructor.
     * @param string $orientation
     */
    public function __construct($orientation = 'HORIZONTAL')
    {
    }
}