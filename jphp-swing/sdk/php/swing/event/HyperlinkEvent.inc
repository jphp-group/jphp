<?php
namespace php\swing\event;

/**
 * Class HyperlinkEvent
 * @package php\swing\event
 */
class HyperlinkEvent extends SimpleEvent {
    /**
     * @readonly
     * @var string
     */
    public $url;

    /**
     * @readonly
     * @var string
     */
    public $description;

    /**
     * @readonly
     * @var string[]
     */
    public $attributes;
}
