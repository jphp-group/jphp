<?php
namespace php\gui\event;

use php\gui\UXDragboard;
use php\gui\UXNode;

/**
 * Class UXDragEvent
 * @package php\gui\event
 * @packages gui, javafx
 */
class UXDragEvent extends UXEvent
{
    /**
     * @readonly
     * @var double
     */
    public $sceneX;

    /**
     * @readonly
     * @var double
     */
    public $sceneY;

    /**
     * @readonly
     * @var double
     */
    public $screenX;

    /**
     * @readonly
     * @var double
     */
    public $screenY;

    /**
     * @readonly
     * @var double
     */
    public $x;

    /**
     * @readonly
     * @var double
     */
    public $y;

    /**
     * @readonly
     * @var bool
     */
    public $accepted;

    /**
     * @readonly
     * @var string
     */
    public $acceptedTransferMode;

    /**
     * @readonly
     * @var string
     */
    public $transferMode;

    /**
     * @var bool
     */
    public $dropCompleted;

    /**
     * @var mixed|UXNode
     */
    public $gestureSource;

    /**
     * @var mixed|UXNode
     */
    public $gestureTarget;

    /**
     * @var UXDragboard
     */
    public $dragboard;

    public function acceptTransferModes(array $modes)
    {
    }
}