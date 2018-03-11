<?php
namespace php\gui;

/**
 * Class UXSplitPane
 * @package php\gui
 * @packages gui, javafx
 */
class UXSplitPane extends UXControl
{
    /**
     * @var UXList list of UXNode
     */
    public $items;

    /**
     * @var string
     */
    public $orientation;

    /**
     * @var double[]
     */
    public $dividerPositions = [];

    /**
     * @param UXNode[] $items (optional)
     */
    public function __construct(array $items)
    {
    }

    /**
     * @param int $index
     * @param double $position
     */
    public function setDividerPosition($index, $position)
    {
    }

    /**
     * @param UXNode $node
     * @param bool $value
     */
    public static function setResizeWithParent(UXNode $node, $value)
    {
    }
}