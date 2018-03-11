<?php
namespace php\gui\shape;
use php\gui\UXList;


/**
 * Class UXEllipse
 * @package php\gui\shape
 * @packages gui, javafx
 */
class UXPolygon extends UXShape
{
    /**
     * @readonly
     * @var UXList of double
     */
    public $points;

    /**
     * UXPolygon constructor.
     * @param double[] $points (optional)
     */
    public function __construct(array $points)
    {
    }
}