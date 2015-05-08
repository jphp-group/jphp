<?php
namespace ext\javafx\paint;

/**
 * Class UXColor
 * @package ext\javafx\paint
 */
class UXColor
{
    /**
     * @readonly
     * @var double
     */
    public $red;

    /**
     * @readonly
     * @var double
     */
    public $blue;

    /**
     * @readonly
     * @var double
     */
    public $green;

    /**
     * @readonly
     * @var double
     */
    public $opacity;

    /**
     * @readonly
     * @var double
     */
    public $brightness;

    /**
     * @readonly
     * @var double
     */
    public $hue;

    /**
     * @readonly
     * @var double
     */
    public $saturation;

    /**
     * @param double $r
     * @param double $g
     * @param double $b
     * @param double $opacity (optional)
     */
    public function __construct($r, $g, $b, $opacity) {}

    /**
     * @return UXColor
     */
    public function grayscale() {}

    /**
     * @return UXColor
     */
    public function invert() {}

    /**
     * @return UXColor
     */
    public function saturate() {}

    /**
     * @return UXColor
     */
    public function desaturate() {}

    /**
     * @param UXColor $color
     * @param double $t
     * @return UXColor
     */
    public function interpolate(UXColor $color, $t) {}

    /**
     * @param string $colorString
     * @return UXColor
     */
    public static function of($colorString) {}

    /**
     * @param int $r
     * @param int $g
     * @param int $b
     * @param double $opacity (optional)
     * @return UXColor
     */
    public static function rgb($r, $g, $b, $opacity) {}
}