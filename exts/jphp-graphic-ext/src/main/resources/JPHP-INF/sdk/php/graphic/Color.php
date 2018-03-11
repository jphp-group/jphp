<?php
namespace php\graphic;

/**
 * Class Color
 * @package php\graphic
 *
 *
 * @packages graphic
 */
class Color
{
    /**
     * @readonly
     * @var int
     */
    public $red = 0;

    /**
     * @readonly
     * @var int
     */
    public $green = 0;

    /**
     * @readonly
     * @var int
     */
    public $blue = 0;

    /**
     * @readonly
     * @var int
     */
    public $alpha = 0;

    /**
     * @readonly
     * @var int
     */
    public $rgb = 0;

    /**
     * Color constructor.
     * @param string|int $value
     */
    public function __construct($value)
    {
    }

    /**
     * @param int $r
     * @param int $g
     * @param int $b
     * @param float $alpha
     * @return Color
     */
    public static function ofRGB(int $r, int $g, int $b, float $alpha = 1.0): Color
    {
    }

    /**
     * @return Color
     */
    public function brighter(): Color
    {
    }

    /**
     * @return Color
     */
    public function darker(): Color
    {
    }

    /**
     * @return string
     */
    public function toHexString(): string
    {
    }
}