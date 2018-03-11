<?php
namespace php\gui\effect;

use php\gui\paint\UXColor;

/**
 * Class UXGlowEffect
 * @package php\gui\effect
 * @packages gui, javafx
 */
class UXGlowEffect extends UXEffect
{
    /**
     * @var double   0.0 to 1.0
     */
    public $level = 0.3;

    /**
     * @param double $level (optional)
     */
    public function __construct($level)
    {
    }
}