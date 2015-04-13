<?php
namespace php\gdx\math;

/**
 * Class Vector2
 * @package php\gdx\math
 */
class Vector2 {
    /**
     * @param float|Vector2 $x (optional)
     * @param float $y (optional)
     */
    public function __construct($x, $y) { }

    /**
     * @return float
     */
    public function x() { }

    /**
     * @return float
     */
    public function y() { }

    /**
     * @return float The euclidean length
     */
    public function len () { }

    /**
     * @return float The squared euclidean length
     */
    public function len2 () { }

    /**
     * Sets this vector from the given vector or x, y
     *
     * @param float|Vector2 $x
     * @param float $y (optional)
     * @return Vector2
     */
    public function set ($x, $y) { }

    /**
     * Subtracts the given vector from this vector.
     *
     * @param float|Vector2 $x
     * @param float $y (optional)
     * @return Vector2
     */
    public function sub ($x, $y) { }

    /**
     * Normalizes this vector. Does nothing if it is zero.
     *
     * @return Vector2
     */
    public function nor () { }

    /**
     * Adds the given vector to this vector
     *
     * @param float|Vector2 $x
     * @param float $y (optional)
     */
    public function add ($x, $y) { }

    /**
     * @param float|Vector2 $ox
     * @param float $oy (optional)
     * @return float The dot product between this and the other vector
     */
    public function dot ($ox, $oy) { }

    /**
     * Scales this vector by a scalar
     *
     * @param float $x
     * @param float $y (optional)
     * @return Vector2
     */
    public function scl ($x, $y) { }

    /**
     * First scale a supplied vector, then add it to this vector.
     *
     * @param Vector2 $vec
     * @param float $scalar
     * @return Vector2
     */
    public function mulAdd ($vec, $scalar) { }

    /**
     * @param float|Vector2 $x
     * @param float $y (optional)
     * @return float the distance between this and the other vector
     */
    public function dst ($x, $y) { }

    /**
     * @param float|Vector2 $x
     * @param float $y (optional)
     * @return float the squared distance between this and the other vector
     */
    public function dst2 ($x, $y) { }

    /**
     * @param float $limit
     * @return Vector2
     */
    public function limit ($limit) { }

    /**
     * @param float $min
     * @param float $max
     * @return Vector2
     */
    public function clamp ($min, $max) { }

    /**
     * @param float|Vector2 $x
     * @param float $y (optional)
     * @return float
     */
    public function crs ($x, $y) { }

    /**
     * @return float
     */
    public function angle () { }

    /**
     * @return float
     */
    public function getAngleRad () { }

    /**
     * @param float $degrees
     * @return Vector2
     */
    public function setAngle ($degrees) { }

    /**
     * @param float $radians
     * @return Vector2
     */
    public function setAngleRad ($radians) { }

    /**
     * @param float $degrees
     * @return Vector2
     */
    public function rotate ($degrees) { }

    /**
     * @param float $radians
     * @return Vector2
     */
    public function rotateRad ($radians) { }

    /**
     * @param int $dir
     * @return Vector2
     */
    public function rotate90 ($dir) { }

    /**
     * Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
     * in this vector.
     *
     * @param Vector2 $target
     * @param float $alpha
     * @return Vector2
     */
    public function lerp (Vector2 $target, $alpha) { }

    /**
     * Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
     *
     * @param Vector2 $other
     * @param float $epsilon
     * @return bool
     */
    public function epsilonEquals (Vector2 $other, $epsilon) { }

    /**
     * Whether this vector is a unit length vector
     *
     * @param float $margin (optional)
     * @return bool
     */
    public function isUnit ($margin) { }

    /**
     * @param float $margin (optional)
     */
    public function isZero ($margin) { }

    /**
     * @param Vector2 $other
     * @param float $epsilon (optional)
     * @return bool
     */
    public function isOnLine (Vector2 $other, $epsilon) { }

    /**
     * @param Vector2 $other
     * @param float $epsilon (optional)
     * @return bool
     */
    public function isCollinear (Vector2 $other, $epsilon) { }

    /**
     * @param Vector2 $other
     * @param float $epsilon (optional)
     * @return bool
     */
    public function isCollinearOpposite (Vector2 $other, $epsilon) { }

    /**
     * @param Vector2 $vector
     * @param float $epsilon (optional)
     */
    public function isPerpendicular (Vector2 $vector, $epsilon) { }

    /**
     * @param Vector2 $vector
     * @return bool
     */
    public function hasSameDirection (Vector2 $vector) { }

    /**
     * @param Vector2 $vector
     * @return bool
     */
    public function hasOppositeDirection (Vector2 $vector) { }
} 