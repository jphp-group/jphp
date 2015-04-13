<?php
namespace php\gdx\math;

/**
 * Class Polygon
 * @package php\gdx\math
 */
class Polygon {
    /**
     * @param float[] $vertices (optional)
     */
    public function __construct(array $vertices) { }

    /**
     * Returns the polygon's local vertices without scaling or rotation and without being offset by the polygon position.
     * @return float[]
     */
    public function getVertices() { }

    /**
     * @return float[] vertices scaled, rotated, and offset by the polygon position.
     */
    public function getTransformedVertices() { }

    /**
     * Sets the origin point to which all of the polygon's local vertices are relative to.
     *
     * @param float $originX
     * @param float $originY
     */
    public function setOrigin($originX, $originY) { }

    /**
     * Sets the polygon's position within the world.
     *
     * @param float $x
     * @param float $y
     */
    public function setPosition($x, $y) { }

    /**
     * @param float[] $vertices
     */
    public function setVertices(array $vertices) { }

    /**
     * Translates the polygon's position by the specified horizontal and vertical amounts.
     * @param float $x
     * @param float $y
     */
    public function translate($x, $y) { }

    /**
     * Sets the polygon to be rotated by the supplied degrees.
     * @param float $degrees
     */
    public function setRotation($degrees) { }

    /**
     * Applies additional rotation to the polygon by the supplied degrees.
     * @param float $degrees
     */
    public function rotate($degrees) { }

    /**
     * Sets the amount of scaling to be applied to the polygon.
     * @param float $scaleX
     * @param float $scaleY
     */
    public function setScale($scaleX, $scaleY) { }

    /**
     * Applies additional scaling to the polygon by the supplied amount.
     * @param float $amount
     */
    public function scale($amount) { }

    /**
     * Sets the polygon's world vertices to be recalculated when calling getTransformedVertices().
     */
    public function dirty() { }

    /**
     * Returns the area contained within the polygon.
     * @return float
     */
    public function area() { }

    /**
     * @param float $x
     * @param float $y
     * @return bool
     */
    public function contains($x, $y) { }

    /**
     * @return float
     */
    public function getX() { }

    /**
     * @return float
     */
    public function getY() { }

    /**
     * @return float
     */
    public function getOriginX() { }

    /**
     * @return float
     */
    public function getOriginY() { }

    /**
     * @return float
     */
    public function getRotation() { }

    /**
     * @return float
     */
    public function getScaleX() { }

    /**
     * @return float
     */
    public function getScaleY() { }
}