<?php
namespace php\swing\event;

use php\swing\UIElement;

/**
 * Class ComponentEvent
 * @package php\swing\event
 */
abstract class ComponentEvent {
    /**
     * @readonly
     * @var UIElement
     */
    public $target;
}
