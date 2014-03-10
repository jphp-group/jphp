<?php
namespace php\swing\event;

use php\swing\UIElement;

/**
 * Events:
 *      - changed
 *
 * Class CaretEvent
 * @package php\swing\event
 */
class ItemEvent {
    /**
     * @readonly
     * @var string
     */
    public $item;

    /**
     * @readonly
     * @var UIElement
     */
    public $target;

    /**
     * @return bool
     */
    public function isSelected() { }

    /**
     * @return bool
     */
    public function isDeselected() { }
}
