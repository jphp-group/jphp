<?php
namespace php\gdx\graphics;


/**
 * Class Sprite
 * @package php\gdx\graphics
 */
class Sprite {
    /**
     * @param Texture $texture (optional)
     * @param int $x (optional)
     * @param int $y (optional)
     * @param int $width (optional)
     * @param int $height (optional)
     */
    public function __construct(Texture $texture, $width, $height, $x, $y) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $width
     * @param int $height
     */
    public function setBounds($x, $y, $width, $height) { }

    /**
     * @param int $width
     * @param int $height
     */
    public function setSize($width, $height) { }

    /**
     * @param int $x
     * @param int $y
     */
    public function setPosition($x, $y) { }

    /**
     * @param int $x
     */
    public function setX($x) { }

    /**
     * @return int
     */
    public function getX() { }

    /**
     * @param int $y
     */
    public function setY($y) { }

    /**
     * @return int
     */
    public function getY() { }

    /**
     * @param double $xAmount
     */
    public function translateX($xAmount) { }

    /**
     * @param double $yAmount
     */
    public function translateY($yAmount) { }

    /**
     * @param double $xAmount
     * @param double $yAmount
     */
    public function translate($xAmount, $yAmount) { }

    /**
     * @param double $alpha
     */
    public function setAlpha($alpha) { }

    /**
     * @param int $originX
     * @param int $originY
     */
    public function setOrigin($originX, $originY) { }

    public function setOriginCenter() { }

    /**
     * @param double $degrees
     */
    public function setRotation($degrees) { }

    /**
     * @return double
     */
    public function getRotation() { }

    /**
     * @param double $degrees
     */
    public function rotate($degrees) { }

    /**
     * @param bool $clockwise
     */
    public function rotate90($clockwise) { }

    /**
     * @param double $scaleXY
     * @param double $scaleY (optional)
     */
    public function setScale($scaleXY, $scaleY) { }

    /**
     * @param double $amount
     */
    public function scale($amount) { }

    /**
     * @param int $x
     * @param int $y
     */
    public function flip($x, $y) { }

    /**
     * @param double $xAmount
     * @param double $yAmount
     */
    public function scroll($xAmount, $yAmount) { }

    /**
     * @param SpriteBatch $batch
     * @param double $alphaModulation (optional)
     */
    public function draw(SpriteBatch $batch, $alphaModulation) {}
} 