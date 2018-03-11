<?php
namespace php\gui\effect;

use php\gui\paint\UXColor;

/**
 * Class UXLightingEffect
 * @package php\gui\effect
 * @packages gui, javafx
 */
class UXLightingEffect extends UXEffect
{
    /**
     * @var double 0.0 to 2.0
     */
    public $diffuseConstant = 1;

    /**
     * @var double 0.0 to 2.0
     */
    public $specularConstant = 0.3;

    /**
     * @var float 0.0 to 40.0
     */
    public $specularExponent = 20.0;

    /**
     * @var float 0 to 10
     */
    public $surfaceScale = 1.5;

    /**
     * @var UXEffect
     */
    public $contentInput = null;

    /**
     * @var UXEffect
     */
    public $bumpInput = null;

    /**
     * @param double $threshold (optional)
     */
    public function __construct($threshold)
    {
    }
}