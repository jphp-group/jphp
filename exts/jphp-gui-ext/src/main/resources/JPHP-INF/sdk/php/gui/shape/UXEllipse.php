<?php
namespace php\gui\shape;


/**
 * Class UXEllipse
 * @package php\gui\shape
 * @packages gui, javafx
 */
class UXEllipse extends UXShape
{
    /**
     * @var double
     */
    public $radiusX;

    /**
     * @var double
     */
    public $radiusY;

    /**
     * UXEllipse constructor.
     * @param float $radiusX (optional)
     * @param float $radiusY (optional)
     */
    public function __construct($radiusX, $radiusY)
    {
    }
}