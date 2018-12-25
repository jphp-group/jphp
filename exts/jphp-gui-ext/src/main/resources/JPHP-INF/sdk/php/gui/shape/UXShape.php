<?php
namespace php\gui\shape;

use php\gui\paint\UXColor;
use php\gui\paint\UXPaint;
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
     * @var UXPaint
     */
    public $fill;

    /**
     * @var UXPaint
     */
    public $stroke;

    /**
     * @var double
     */
    public $strokeWidth = 1;

    /**
     * @var string INSIDE, OUTSIDE, CENTERED
     */
    public $strokeType = 'OUTSIDE';
}