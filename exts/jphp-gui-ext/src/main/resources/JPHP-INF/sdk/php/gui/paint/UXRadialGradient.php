<?php


namespace php\gui\paint;


/**
 * Class UXRadialGradient
 * @package php\gui\paint
 * @packages gui, javafx
 */
class UXRadialGradient extends UXPaint
{
    /**
     * @readonly
     * @var double
     */
    public $centerX;
    /**
     * @readonly
     * @var double
     */
    public $centerY;
    /**
     * @readonly
     * @var double
     */
    public $focusAngle;
    /**
     * @readonly
     * @var double
     */
    public $focusDistance;
    /**
     * @readonly
     * @var double
     */
    public $radius;

    /**
     * @readonly
     * @var UXStop[]
     */
    public $stops;
    /**
     * @readonly
     * @var boolean
     */
    public $proportional;
    /**
     * @readonly
     * @var string
     * NO_CYCLE, REFLECT, REPEAT
     */
    public $cycleMethod;


    /**
     * UXRadialGradient constructor.
     * @param float $focusAngle
     * @param float $focusDistance
     * @param float $centerX
     * @param float $centerY
     * @param float $radius
     * @param float $proportional
     * @param string $cycleMethod
     * @param array $stops
     */
    public function __construct($focusAngle, $focusDistance, $centerX, $centerY, $radius, $proportional, $cycleMethod, $stops){

    }

    /**
     * @param string $value
     * @return UXRadialGradient
     */
    public static function of($value){}
}