<?php
namespace php\graphic;

/**
 * Class ImageDraw
 * @package php\image
 *
 * @packages graphic
 */
class ImageDraw
{
    /**
     * @var Color|string
     */
    public $fill = null;

    /**
     * @var Font
     */
    public $font = null;

    /**
     * @var bool
     */
    public $antialiasing = true;

    /**
     * GASP, LCD_*, true, false
     * @var bool|string
     */
    public $textAntialiasing = true;

    /**
     * ImageDraw constructor.
     * @param Image $image
     */
    public function __construct(Image $image)
    {
    }

    /**
     * @param float $x
     * @param float $y
     * @param float $width
     * @param float $height
     */
    public function clipRect(float $x, float $y, float $width, float $height)
    {
    }

    /**
     * @param float $x
     * @param float $y
     * @param float $width
     * @param float $height
     */
    public function clipEllipse(float $x, float $y, float $width, float $height)
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @param Image $image
     */
    public function image(int $x, int $y, Image $image)
    {
    }

    /**
     * @param string $text
     * @param array $options
     * @return float[]
     */
    public function textSize(string $text, array $options = ['font' => null]): array
    {
    }

    /**
     * @param float $x
     * @param float $y
     * @param string $text
     * @param array $options
     */
    public function text(float $x, float $y, string $text, array $options = ['fill' => null, 'font' => null])
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param int $startAngle
     * @param int $arcAngle
     * @param array $options
     */
    public function arc(int $x, int $y, int $width, int $height, int $startAngle, int $arcAngle, array $options = ['fill' => null, 'outline' => false])
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @param array $options
     */
    public function point(int $x, int $y, array $options = ['fill' => null])
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param array $options
     */
    public function rect(int $x, int $y, int $width, int $height, array $options = ['fill' => null, 'outline' => false])
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param int $arcWidth
     * @param int $arcHeight
     * @param array $options
     */
    public function roundRect(int $x, int $y, int $width, int $height, int $arcWidth, int $arcHeight, array $options = ['fill' => null, 'outline' => false])
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param array $options
     */
    public function ellipse(int $x, int $y, int $width, int $height, array $options = ['fill' => null, 'outline' => false])
    {
    }

    /**
     * @param int $x
     * @param int $y
     * @param int $radius
     * @param array $options
     */
    public function circle(int $x, int $y, int $radius, array $options = ['fill' => null, 'outline' => false])
    {
    }

    /**
     * Disposes of this graphics context and releases
     * any system resources that it is using.
     */
    public function dispose()
    {
    }

    public function __destruct()
    {
        $this->dispose();
    }
}