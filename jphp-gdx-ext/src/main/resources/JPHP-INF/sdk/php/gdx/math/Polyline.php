<?php
namespace php\gdx\math;

/**
 * Class Polyline
 * @package php\gdx\math
 */
class Polyline {
    /**
     * @param float[] $vertices (optional)
     */
    public function __construct(array $vertices) { }

    /**
     * Returns the polyline's local vertices without scaling or rotation and without being offset by the polyline position.
     * @return float[]
     */
    public function getVertices() { }

    /**
     * @return float[] vertices scaled, rotated, and offset by the polyline position.
     */
    public function getTransformedVertices() { }

    /**
     * @return float Returns the euclidean length of the polyline without scaling
     */
    public function getLength() { }

    /**
     * @return float Returns the euclidean length of the polyline
     */
    public function getScaledLength() { }

    /**
     *
     */
    public function calculateLength() { }

    /**
     *
     */
    public function calculateScaledLength() { }

    /**
     * Sets the polyline's world vertices to be recalculated when calling getTransformedVertices().
     */
    public function dirty() { }

    /**
     * Sets the origin point to which all of the polyline's local vertices are relative to.
     *
     * @param float $originX
     * @param float $originY
     */
    public function setOrigin($originX, $originY) { }

    /**
     * Sets the polyline's position within the world.
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
     * Translates the polyline's position by the specified horizontal and vertical amounts.
     * @param float $x
     * @param float $y
     */
    public function translate($x, $y) { }

    /**
     * Sets the polyline to be rotated by the supplied degrees.
     * @param float $degrees
     */
    public function setRotation($degrees) { }

    /**
     * Applies additional rotation to the polyline by the supplied degrees.
     * @param float $degrees
     */
    public function rotate($degrees) { }

    /**
     * Sets the amount of scaling to be applied to the polyline.
     * @param float $scaleX
     * @param float $scaleY
     */
    public function setScale($scaleX, $scaleY) { }

    /**
     * Applies additional scaling to the polyline by the supplied amount.
     * @param float $amount
     */
    public function scale($amount) { }

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