<?php
namespace php\gui\shape;

use php\gui\paint\UXColor;
use php\gui\UXNode;

/**
 * Class UXShape
 * @package php\gui\shape
 * @packages gui, javafx
 */
abstract class UXShape extends UXNode
{
    /**
     * @var bool
     */
    public $smooth = true;

    /**
     * @var UXColor
     */
    public $fillColor;

    /**
     * @var UXColor
     */
    public $strokeColor;

    /**
     * @var double
     */
    public $strokeWidth = 1;

    /**
     * @var string INSIDE, OUTSIDE, CENTERED
     */
    public $strokeType = 'OUTSIDE';
}