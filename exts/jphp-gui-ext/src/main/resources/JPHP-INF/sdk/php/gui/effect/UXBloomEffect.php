<?php
namespace php\gui\effect;

use php\gui\paint\UXColor;

/**
 * Class UXBloomEffect
 * @package php\gui\effect
 * @packages gui, javafx
 */
class UXBloomEffect extends UXEffect
{
    /**
     * @var double
     */
    public $threshold = 0.3;

    /**
     * @param double $threshold (optional)
     */
    public function __construct($threshold)
    {
    }
}