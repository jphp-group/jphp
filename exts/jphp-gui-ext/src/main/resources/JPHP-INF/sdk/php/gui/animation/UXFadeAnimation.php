<?php
namespace php\gui\animation;

use php\gui\UXNode;

/**
 * Class UXFadeAnimation
 * @package php\gui\animation
 *
 * @packages gui, javafx
 */
class UXFadeAnimation extends UXAnimation
{
    /**
     * @var double
     */
    public $fromValue;

    /**
     * @var double
     */
    public $toValue;

    /**
     * @var double
     */
    public $byValue;

    /**
     * @var int millis
     */
    public $duration;

    /**
     * @var UXNode
     */
    public $node;

    /**
     * @param int $duration (optional)
     * @param UXNode $node (optional)
     */
    public function __construct($duration, UXNode $node)
    {
    }
    
}