<?php
namespace php\gui\effect;

use php\gui\paint\UXColor;

/**
 * Class UXDropShadowEffect
 * @package php\gui\effect
 *
 * @packages gui, javafx
 */
class UXDropShadowEffect extends UXEffect
{
    /**
     * @var string ONE_PASS_BOX, TWO_PASS_BOX, THREE_PASS_BOX, GAUSSIAN
     */
    public $blurType = 'THREE_PASS_BOX';

    /**
     * @var UXColor
     */
    public $color;

    /**
     * @var double
     */
    public $radius;

    /**
     * @var float
     */
    public $offsetX = 0.0;

    /**
     * @var float
     */
    public $offsetY = 0.0;

    /**
     * @var float
     */
    public $spread = 0.0;

    /**
     * @var double
     */
    public $width;

    /**
     * @var double
     */
    public $height;

    /**
     * @var float[] width + height
     */
    public $size = [0.0, 0.0];

    /**
     * @param double $radius [optional]
     * @param UXColor|string $color [optional]
     * @param double $offsetX [optional]
     * @param double $offsetY [optional]
     */
    public function __construct($radius, $color, $offsetX, $offsetY)
    {
    }
}