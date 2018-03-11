<?php
namespace php\gui;
use php\gui\event\UXEvent;

/**
 * Class UXMenuItem
 * @package php\gui
 * @packages gui, javafx
 */
class UXMenuItem
{
    /**
     * @var string
     */
    public $id;

    /**
     * @var string
     */
    public $text;

    /**
     * @var UXNode
     */
    public $graphic;

    /**
     * @var string
     */
    public $style;

    /**
     * @var UXList
     */
    public $classes;

    /**
     * @var string
     */
    public $accelerator;

    /**
     * @var UXContextMenu
     */
    public $parentPopup;

    /**
     * @var bool
     */
    public $visible;

    /**
     * @depricated
     * @var bool
     */
    public $disable;

    /**
     * @var bool
     */
    public $enabled;

    /**
     * @var mixed
     */
    public $userData = null;

    /**
     * @param string $text [optional]
     * @param UXNode $graphic
     */
    public function __construct($text, UXNode $graphic = null) { }

    /**
     * @param string $event
     * @param callable $handler
     * @param string $group
     */
    public function on($event, callable $handler, $group = 'general') { }

    /**
     * @param string $event
     * @param string $group (optional)
     */
    public function off($event, $group) {}

    /**
     * @param string $event
     * @param UXEvent $e (optional)
     */
    public function trigger($event, UXEvent $e) {}

    /**
     * @return bool
     */
    public function isSeparator()
    {
    }

    /**
     * @return UXMenuItem
     */
    public static function createSeparator()
    {
    }
}