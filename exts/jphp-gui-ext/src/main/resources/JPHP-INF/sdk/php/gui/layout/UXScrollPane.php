<?php
namespace php\gui\layout;

use php\gui\UXControl;
use php\gui\UXNode;

/**
 * Class UXScrollPane
 * @package php\gui\layout
 * @packages gui, javafx
 */
class UXScrollPane extends UXControl
{
    /**
     * @var UXNode
     */
    public $content;

    /**
     * @var double
     */
    public $scrollX;

    /**
     * @var double
     */
    public $scrollY;

    /**
     * @readonly
     * @var array
     */
    public $viewportBounds = ['x' => 0.0, 'y' => 0.0, 'z' => 0.0, 'width' => 0.0, 'height' => 0.0];

    /**
     * @var double
     */
    public $scrollMinX = 0, $scrollMinY = 0;

    /**
     * @var float
     */
    public $scrollMaxX = 1.0, $scrollMaxY = 1.0;

    /**
     * @var bool
     */
    public $fitToWidth = false;

    /**
     * @var bool
     */
    public $fitToHeight = false;

    /**
     * @var string AS_NEEDED, ALWAYS, NEVER
     */
    public $vbarPolicy = 'AS_NEEDED';

    /**
     * @var string AS_NEEDED, ALWAYS, NEVER
     */
    public $hbarPolicy = 'AS_NEEDED';

    /**
     * @param UXNode $node (optional)
     */
    public function __construct(UXNode $node)
    {
    }

    /**
     * @param UXNode $node
     */
    public function scrollToNode(UXNode $node)
    {
    }
}