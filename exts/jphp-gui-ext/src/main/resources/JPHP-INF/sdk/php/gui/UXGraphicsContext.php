<?php
namespace php\gui;
use php\gui\effect\UXEffect;
use php\gui\paint\UXColor;
use php\gui\text\UXFont;

/**
 * Class UXGraphicsContext
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXGraphicsContext
{
    /**
     * @readonly
     * @var UXCanvas
     */
    public $canvas;

    /**
     * @var UXFont
     */
    public $font;

    /**
     * @var float
     */
    public $globalAlpha = 1.0;

    /***
     * SRC_OVER, SRC_ATOP, ADD, MULTIPLY, SCREEN, OVERLAY, DARKEN, LIGHTEN, COLOR_DODGE, COLOR_BURN
     * @var string
     */
    public $globalBlendMode = null;

    /**
     * @var double
     */
    public $lineWidth;

    /**
     * SQUARE, BUTT, ROUND
     * @var string
     */
    public $lineCap;

    /**
     * MITER, BEVEL, BEVEL
     * @var string
     */
    public $lineJoin;

    /**
     * @var double
     */
    public $miterLimit;

    /**
     * @var UXColor
     */
    public $fillColor;

    /**
     * EVEN_ODD, NON_ZERO
     * @var string
     */
    public $fillRule;

    /**
     * @var UXColor
     */
    public $strokeColor;


    /**
     * Resets the current path to empty.
     * The default path is empty.
     */
    public function beginPath()
    {
    }

    /**
     * Issues a move command for the current path to the given x,y coordinate.
     * The coordinates are transformed by the current transform as they are
     * added to the path and unaffected by subsequent changes to the transform.
     *
     * @param double $x0
     * @param double $y0
     */
    public function moveTo($x0, $y0)
    {
    }

    /**
     * Adds segments to the current path to make a line to the given x,y
     * coordinate.
     * @param double $x1
     * @param double $y1
     */
    public function lineTo($x1, $y1)
    {
    }

    /**
     * Adds segments to the current path to make a quadratic Bezier curve.
     *
     * @param double $xc
     * @param double $yc
     * @param double $x1
     * @param double $y1
     */
    public function quadraticCurveTo($xc, $yc, $x1, $y1)
    {
    }

    /**
     * Adds segments to the current path to make a cubic Bezier curve.
     *
     * @param double $xc1
     * @param double $yc1
     * @param double $xc2
     * @param double $yc2
     * @param double $x1
     * @param double $y1
     */
    public function bezierCurveTo($xc1, $yc1, $xc2, $yc2, $x1, $y1)
    {
    }

    /**
     * Adds segments to the current path to make an arc.
     *
     * @param double $x1
     * @param double $y1
     * @param double $x2
     * @param double $y2
     * @param double $radius
     */
    public function arcTo($x1, $y1, $x2, $y2, $radius)
    {
    }

    /**
     * Adds path elements to the current path to make an arc that uses Euclidean
     * degrees. This Euclidean orientation sweeps from East to North, then West,
     * then South, then back to East.
     *
     * @param double $centerX
     * @param double $centerY
     * @param double $radiusX
     * @param double $radiusY
     * @param double $startAngle
     * @param double $length
     */
    public function arc($centerX, $centerY, $radiusX, $radiusY, $startAngle, $length)
    {
    }

    /**
     * Adds path elements to the current path to make a rectangle.
     *
     * @param double $x
     * @param double $y
     * @param double $w
     * @param double $h
     */
    public function rect($x, $y, $w, $h)
    {
    }

    /**
     * Appends an SVG Path string to the current path. If there is no current
     * path the string must then start with either type of move command.
     *
     * @param string $svgpath
     */
    public function appendSVGPath($svgpath)
    {
    }

    /**
     * Closes the path.
     */
    public function closePath()
    {
    }

    /**
     * Fills the path with the current fill paint.
     */
    public function fill()
    {
    }

    /**
     * Strokes the path with the current stroke paint.
     */
    public function stroke()
    {
    }

    /**
     * Intersects the current clip with the current path and applies it to
     * subsequent rendering operation as an anti-aliased mask.
     */
    public function clip()
    {
    }

    /**
     * Returns true if the the given x,y point is inside the path.
     *
     * @param double $x
     * @param double $y
     * @return bool
     */
    public function isPointInPath($x, $y)
    {
    }

    /**
     * Clears a portion of the canvas with a transparent color value.
     *
     * @param double $x
     * @param double $y
     * @param double $w
     * @param double $h
     */
    public function clearRect($x, $y, $w, $h)
    {
    }

    /**
     * @param double $x
     * @param double $y
     * @param double $w
     * @param double $h
     */
    public function fillRect($x, $y, $w, $h)
    {
    }

    /**
     * Fills the given string of text at position x, y
     * with the current fill paint attribute.
     *
     * @param string $text
     * @param double $x
     * @param double $y
     * @param float|int $maxWidth
     */
    public function fillText($text, $x, $y, $maxWidth = 0)
    {
    }

    /**
     * Draws the given string of text at position x, y
     * with the current fill paint attribute.
     *
     * @param string $text
     * @param double $x
     * @param double $y
     * @param float|int $maxWidth
     */
    public function strokeText($text, $x, $y, $maxWidth = 0)
    {
    }

    /**
     * @param UXImage $image
     * @param $x
     * @param $y
     * @param $w (optional)
     * @param $h (optional)
     * @param $dx (optional)
     * @param $dy (optional)
     * @param $dw (optional)
     * @param $dh (optional)
     */
    public function drawImage(UXImage $image, $x, $y, $w = null, $h = null, $dx = null, $dy = null, $dw = null, $dh = null) {
    }

    /**
     * @param UXColor $color
     */
    public function setFillColor(UXColor $color)
    {
    }
}