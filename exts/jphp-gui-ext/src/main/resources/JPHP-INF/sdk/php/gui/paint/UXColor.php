<?php
namespace php\gui\paint;

/**
 * Class UXColor
 * @package php\gui\paint
 * @packages gui, javafx
 */
class UXColor
{
    /**
     * Уровень красного (от 0 до 1)
     * @readonly
     * @var double
     */
    public $red;

    /**
     * Уровень синего (от 0 до 1).
     * @readonly
     * @var double
     */
    public $blue;

    /**
     * Уровень зеленого (от 0 до 1).
     * @readonly
     * @var double
     */
    public $green;

    /**
     * Уровень прозрачности (от 0 до 1).
     * @readonly
     * @var double
     */
    public $opacity;

    /**
     * Уровень яркости.
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
     * WEB значение цвета.
     * @readonly
     * @var string
     */
    public $webValue;

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
     * @return int
     */
    public function getRGB()
    {
    }

    /**
     * @return string
     */
    public function getWebValue()
    {
    }

    /**
     * @param string $colorString
     * @return UXColor
     */
    public static function of($colorString) {}

    /**
     * @param int $r
     * @param int $g
     * @param int $b
     * @param double $opacity [optional]
     * @return UXColor
     */
    public static function rgb($r, $g, $b, $opacity) {}
}