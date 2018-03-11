<?php
namespace php\gui\effect;

use php\gui\paint\UXColor;

/**
 * Class UXColorAdjustEffect
 * @package php\gui\effect
 * @packages gui, javafx
 */
class UXColorAdjustEffect extends UXEffect
{
    /**
     * @var double   -1.0 to 1.0
     */
    public $brightness = 0.0;

    /**
     * @var double   -1.0 to 1.0
     */
    public $contrast = 0.0;

    /**
     * @var double   -1.0 to 1.0
     */
    public $hue = 0.0;

    /**
     * @var double   -1.0 to 1.0
     */
    public $saturation = 0.0;
}