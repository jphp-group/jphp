<?php
namespace php\gui\animation;

/**
 * Class UXPathAnimation
 * @package php\gui\animation
 *
 * @packages gui, javafx
 */
class UXPathAnimation extends UXAnimation
{
    /**
     * @var int
     */
    public $duration;

    /**
     * NONE or ORTHOGONAL_TO_TANGENT
     *
     * ORTHOGONAL_TO_TANGENT - The targeted node's rotation matrix is set to keep node
     * perpendicular to the path's tangent along the geometric path.
     *
     * @var string
     */
    public $orientation = 'NONE';


    /**
     * @param int $duration (optional)
     * @param UXNode $node (optional)
     */
    public function __construct($duration, UXNode $node)
    {
    }

    /**
     * @param double $x
     * @param double $y
     * @return $this
     */
    public function addMoveTo($x, $y)
    {
        return $this;
    }

    /**
     * @param double $x
     * @param double $y
     * @return $this
     */
    public function addLineTo($x, $y)
    {
        return $this;
    }

    public function clearPath()
    {
    }
}