<?php
namespace php\swing\event;
use php\swing\UIElement;

/**
 * Events:
 *
 *      * caretUpdate
 *
 * Class CaretEvent
 * @package php\swing\event
 */
class CaretEvent {
    /**
     * @readonly
     * @var int
     */
    public $dot;

    /**
     * @readonly
     * @var int
     */
    public $mark;

    /**
     * @readonly
     * @var UIElement
     */
    public $target;
}
