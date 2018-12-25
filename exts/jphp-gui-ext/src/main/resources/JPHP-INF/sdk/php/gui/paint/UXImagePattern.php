<?php
namespace php\gui\paint;


use php\gui\UXImage;

/**
 * Class UXImagePattern
 * @package php\gui\paint
 * @packages gui, javafx
 */
class UXImagePattern extends UXPaint
{
    /**
     * @readonly
     * @var boolean
     */
    public $width;
    /**
     * @readonly
     * @var boolean
     */
    public $height;
    /**
     * @readonly
     * @var boolean
     */
    public $x;
    /**
     * @readonly
     * @var boolean
     */
    public $y;
    /**
     * @readonly
     * @var boolean
     */
    public $proportional;


    /**
     * UXImagePattern constructor.
     * @param UXImage $image
     * @param float|null $width
     * @param float|null $height
     * @param float|null $x
     * @param float|null $y
     * @param float|null $proportional
     */
    public function __construct($image, $x = null, $y = null, $width = null, $height = null, $proportional = null){
    }
}