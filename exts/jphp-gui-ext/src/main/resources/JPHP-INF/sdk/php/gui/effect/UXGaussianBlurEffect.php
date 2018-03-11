<?php
namespace php\gui\effect;

/**
 * Class UXGaussianBlurEffect
 * @package php\gui\effect
 * @packages gui, javafx
 */
class UXGaussianBlurEffect extends UXEffect
{
    /**
     * @var float
     */
    public $radius = 10.0;

    /**
     * @param double $radius (optional)
     */
    public function __construct($radius)
    {
    }
}