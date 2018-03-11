<?php
namespace php\gui\layout;

use php\gui\UXNode;

/**
 * Class UXVBox
 * @package php\gui\layout
 * @packages gui, javafx
 */
class UXVBox extends UXPane
{
    /**
     * TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, ... CENTER, ... BOTTOM_RIGHT,
     * BASELINE_LEFT, BASELINE_CENTER, BASELINE_RIGHT
     * @var string
     */
    public $alignment;

    /**
     * @var float
     */
    public $spacing;

    /**
     * @var bool
     */
    public $fillWidth = true;

    /**
     * @param UXNode[] $nodes [optional]
     * @param float|int $spacing
     */
    public function __construct(array $nodes, $spacing = 0)
    {
    }

    public function requestLayout()
    {
    }

    /**
     * @param UXNode $node
     * @param string $value
     */
    static public function setVgrow(UXNode $node, $value)
    {
    }

    /**
     * @param UXNode $node
     * @return string
     */
    static public function getVgrow(UXNode $node)
    {
    }

    /**
     * @param UXNode $node
     * @param double[]|double $margins
     */
    static public function setMargin(UXNode $node, $margins)
    {
    }
}