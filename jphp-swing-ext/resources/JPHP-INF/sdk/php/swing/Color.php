<?php
namespace php\swing;


class Color {
    /**
     * @readonly
     * @var int
     */
    public $rgb;

    /**
     * @readonly
     * @var int
     */
    public $alpha;

    /**
     * @readonly
     * @var int
     */
    public $red;

    /**
     * @readonly
     * @var int
     */
    public $green;

    /**
     * @readonly
     * @var int
     */
    public $blue;

    /**
     * @param int $rgb
     * @param bool $hasAlpha
     */
    public function __construct($rgb, $hasAlpha = false){ }

    /**
     * Creates a new Color that is a darker version of this
     * @return Color
     */
    public function darker() { }

    /**
     * Creates a new Color that is a brighter version of this
     * @return Color
     */
    public function brighter() { }

    /**
     * @param int $r - 0 .. 255
     * @param int $g - 0 .. 255
     * @param int $b - 0 .. 255
     * @param int $alpha - 0 .. 255
     * @return Color
     */
    public static function rgb($r, $g, $b, $alpha = 255) { }

    /**
     * @param double $r - between 0 and 1
     * @param double $g - between 0 and 1
     * @param double $b - between 0 and 1
     * @param double $alpha - between 0 and 1
     * @return Color
     */
    public static function floatRgb($r, $g, $b, $alpha = 1.0) { }

    /**
     * Decode color from a hex string (#RGB, 0xRGB, etc...)
     * @param $nm
     * @return Color
     * @return null - return null if $nm is invalid
     */
    public static function decode($nm) {  }
}
