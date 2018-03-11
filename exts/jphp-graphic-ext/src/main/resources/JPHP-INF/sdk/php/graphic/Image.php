<?php
namespace php\graphic;

use php\io\Stream;

/**
 * Class Image
 * @package php\image
 *
 *
 * @packages graphic
 */
class Image
{
    /**
     * @var int
     */
    public $width = 0;

    /**
     * @var int
     */
    public $height = 0;

    /**
     * @var array
     */
    public $size = [0, 0];

    /**
     * Image constructor.
     * @param int $width
     * @param int $height
     */
    public function __construct(int $width, int $height)
    {
    }

    /**
     * @param string|Stream $source
     * @return Image
     */
    public static function open($source): Image
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @return Color
     */
    public function getPixel(int $x, int $y): Color
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @param Color $color
     */
    public function setPixel(int $x, int $y, Color $color)
    {
    }

    /**
     * @param float $angle
     * @return Image $this
     */
    public function rotate(float $angle): Image
    {
    }

    /**
     * @param int $width
     * @param int $height
     * @return Image $this
     */
    public function resize(int $width, int $height): Image
    {
    }

    /**
     * @param string $format
     * @param $dest
     * @return bool
     */
    public function save($dest, string $format): bool
    {
    }

    /**
     * Clonable.
     */
    public function __clone()
    {
    }
}