<?php


namespace php\gui\paint;


/**
 * Class UXLinearGradient
 * @package php\gui\paint
 * @packages gui, javafx
 */
class UXLinearGradient extends UXPaint
{
    /**
     * @readonly
     * @var double
     */
    public $endX;
    /**
     * @readonly
     * @var double
     */
    public $endY;
    /**
     * @readonly
     * @var double
     */
    public $startX;
    /**
     * @readonly
     * @var double
     */
    public $startY;
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
     * UXLinearGradient constructor.
     * @param float $startX
     * @param float $startY
     * @param float $endX
     * @param float $endY
     * @param boolean $proportional
     * @param string $cycleMethod
     * @param array $stops
     */
    public function __construct($startX, $startY, $endX, $endY, $proportional, $cycleMethod, $stops){

    }

    /**
     * @param string $value
     * @return UXLinearGradient
     */
    public static function of($value){}
}