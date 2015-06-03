<?php
namespace php\swing;

/**
 * Class Graphics
 * @package php\swing
 */
class Graphics {
    /**
     * Foreground color
     * @var Color
     */
    public $color;

    /**
     * Font of text
     * @var Font
     */
    public $font;

    /**
     * private
     */
    private function __construct() { }

    /**
     * Return width of str for drawText + current font
     * @param $str
     * @return int
     */
    public function getTextWidth($str) { }

    /**
     * Return height of one line text with current font
     * @return int
     */
    public function getTextHeight() { }

    /**
     * Sets the paint mode of this graphics context to overwrite the
     * destination with this graphics context's current color.
     */
    public function setPaintMode() { }

    /**
     * Draw line
     * @param int $x1
     * @param int $y1
     * @param int $x2
     * @param int $y2
     */
    public function drawLine($x1, $y1, $x2, $y2) { }

    /**
     * Draw rect
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function drawRect($x, $y, $width, $height) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function fillRect($x, $y, $width, $height) { }

    /**
     * Draws a 3-D highlighted outline of the specified rectangle.
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param $raised
     */
    public function draw3DRect($x, $y, $width, $height, $raised) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param $raised
     */
    public function fill3DRect($x, $y, $width, $height, $raised) { }

    /**
     * Draw oval
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function drawOval($x, $y, $width, $height) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function fillOval($x, $y, $width, $height) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param int $startAngle
     * @param int $arcAngle
     */
    public function drawArc($x, $y, $width, $height, $startAngle, $arcAngle) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param int $startAngle
     * @param int $arcAngle
     */
    public function fillArc($x, $y, $width, $height, $startAngle, $arcAngle) { }

    /**
     * @param array $xy - [[x1, y1], [x2, y2], ... ]
     */
    public function drawPolygon(array $xy) { }

    /**
     * @param array $xy - [[x1, y1], [x2, y2], ... ]
     */
    public function fillPolygon(array $xy) { }

    /**
     * @param array $xy - [[x1, y1], [x2, y2], ... ]
     */
    public function drawPolyline(array $xy) { }

    /**
     * @param Image $image
     * @param int $x
     * @param int $y
     * @param null|int $newWidth
     * @param null|int $newHeight
     */
    public function drawImage(Image $image, $x = 0, $y = 0, $newWidth = null, $newHeight = null) { }

    /**
     * @param string $text
     * @param int $x
     * @param int $y
     */
    public function drawText($text, $x, $y) { }

    /**
     * Intersects the current clip with the specified rectangle.
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function clipRect($x, $y, $width, $height) { }

    /**
     * Clears the specified rectangle by filling it with the background
     * color of the current drawing surface.
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function clearRect($x, $y, $width, $height) { }

    /**
     * @param Color|int|array $color
     */
    public function setXORMode($color) { }

    /**
     * Translates the origin of the graphics context to the point
     * (x, y) in the current coordinate system.
     * @param int $x
     * @param int $y
     */
    public function translate($x, $y) { }

    /**
     * Copies an area of the component by a distance specified by
     * $dx and $dy
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     * @param int $dx
     * @param int $dy
     */
    public function copyArea($x, $y, $width, $height, $dx, $dy) { }

    /**
     * Create new copy Graphics from this
     * @param int|null $x
     * @param int|null $y
     * @param int|null $w
     * @param int|null $h
     * @return Graphics
     */
    public function create($x = null, $y = null, $w = null, $h = null) { }

    /**
     * Disposes of this graphics context and releases
     * any system resources that it is using.
     */
    public function dispose() { }
}
