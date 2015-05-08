<?php
namespace ext\javafx;
use ext\javafx\event\UXEvent;

/**
 * Class UXMenuItem
 * @package ext\javafx
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
     * @var bool
     */
    public $disable;

    /**
     * @param string $text (optional)
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
}