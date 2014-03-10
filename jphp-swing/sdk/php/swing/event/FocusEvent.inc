<?php
namespace php\swing\event;

/**
 * Events:
 *      - focus
 *      - blur
 *
 * Class FocusEvent
 * @package php\swing\event
 */
class FocusEvent extends ComponentEvent {
    /**
     * @readonly
     * @var bool
     */
    public $temporary;

    /**
     * @readonly
     * @var string
     */
    public $paramString;
}
