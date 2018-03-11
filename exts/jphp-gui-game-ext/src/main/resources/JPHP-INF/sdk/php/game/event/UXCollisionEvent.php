<?php
namespace php\game\event;

use php\gui\event\UXEvent;

/**
 * ->sender first object
 * ->other second object
 *
 * Class UXCollisionEvent
 * @package php\game\event
 * @packages game, javafx
 */
class UXCollisionEvent extends UXEvent
{
    /**
     * @var float[]
     */
    public $normal = [0, 0];

    /**
     * @var float[]
     */
    public $tangent = [0, 0];

    /**
     * UXCollisionEvent constructor.
     * @param UXCollisionEvent $parent
     * @param mixed $sender
     * @param mixed $target
     */
    public function __construct(UXCollisionEvent $parent, $sender, $target)
    {
    }
}