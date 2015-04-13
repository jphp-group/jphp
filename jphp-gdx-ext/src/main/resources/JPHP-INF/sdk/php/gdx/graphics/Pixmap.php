<?php
namespace php\gdx\graphics;
use php\gdx\files\FileHandle;

/**
 * Class Pixmap
 * @package php\gdx\graphics
 */
class Pixmap {
    /**
     * @param int $width
     * @param int $height
     * @param string $format - Alpha, Intensity, LuminanceAlpha, RGB565, RGBA4444, RGB888, RGBA8888
     */
    public function __construct($width, $height, $format) { }

    /**
     * @param FileHandle $fileHandle
     * @return string
     */
    public static function ofFile(FileHandle $fileHandle) { }

    /**
     * @param double|int $redOrColor
     * @param double $g (optional)
     * @param double $b (optional)
     * @param double $a (optional)
     */
    public function setColor($redOrColor, $g, $b, $a) { }

    /**
     * Fills the complete bitmap with the currently set color.
     */
    public function fill() { }

    /**
     * @param int $x
     * @param int $y
     * @param int $x2
     * @param int $y2
     */
    public function drawLine($x, $y, $x2, $y2) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function drawRectangle($x, $y, $width, $height) { }

    /**
     * @param Pixmap $pixmap
     * @param int $x
     * @param int $y
     * @param int $srcx (optional)
     * @param int $srcy (optional)
     * @param int $srcWidth (optional)
     * @param int $srcHeight (optional)
     */
    public function drawPixmap(Pixmap $pixmap, $x, $y, $srcx, $srcy, $srcWidth, $srcHeight) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function fillRectangle($x, $y, $width, $height) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $radius
     */
    public function drawCircle($x, $y, $radius) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $radius
     */
    public function fillCircle($x, $y, $radius) { }

    /**
     * @param int $x1
     * @param int $y1
     * @param int $x2
     * @param int $y2
     * @param int $x3
     * @param int $y3
     */
    public function fillTriangle($x1, $y1, $x2, $y2, $x3, $y3) { }

    /**
     * @param int $x
     * @param int $y
     * @return int The pixel color in RGBA8888 format.
     */
    public function getPixel($x, $y) { }

    /**
     * @return int
     */
    public function getWidth() { }

    /**
     * @return int
     */
    public function getHeight() { }

    /**
     * Releases all resources associated with this Pixmap.
     */
    public function dispose() { }

    /**
     * @param int $x
     * @param int $y
     * @param int $color (optional)
     */
    public function drawPixel($x, $y, $color) { }

    /**
     * @return int one of GL_ALPHA, GL_RGB, GL_RGBA, GL_LUMINANCE, or GL_LUMINANCE_ALPHA.
     */
    public function getGLFormat() { }

    /**
     * @return int one of GL_ALPHA, GL_RGB, GL_RGBA, GL_LUMINANCE, or GL_LUMINANCE_ALPHA.
     */
    public function getGLInternalFormat() { }

    /**
     * @return int one of GL_UNSIGNED_BYTE, GL_UNSIGNED_SHORT_5_6_5, GL_UNSIGNED_SHORT_4_4_4_4
     */
    public function getGLType() { }

    /**
     * @return string
     */
    public function getFormat() { }

    /**
     * @return string None, SourceOver
     */
    public function getBlending() { }

    /**
     * Sets the type of Blending to be used for all operations. Default is 'SourceOver'
     *
     * @param string $blending
     */
    public static function setBlending($blending) { }

    /**
     * Filters to be used with Pixmap.drawPixmap(Pixmap, int, int, int, int, int, int, int, int).
     *
     * @param string $filter NearestNeighbour, BiLinear
     */
    public static function setFilter($filter) { }
}