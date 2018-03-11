<?php
namespace php\gui\event;

use php\gui\UXWindow;

/**
 * Class UXWindowEvent
 * @package php\gui\event
 * @packages gui, javafx
 */
class UXWindowEvent extends UXEvent {
    /**
     * @var UXWindow
     */
    public $target;

    /**
     * @param UXWindow $sender
     * @param string $eventName
     * @return UXWindowEvent
     */
    public static function makeMock(UXWindow $sender, $eventName = 'any')
    {
    }
}