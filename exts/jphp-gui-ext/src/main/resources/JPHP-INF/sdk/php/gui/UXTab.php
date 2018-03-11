<?php
namespace php\gui;

/**
 * Class UXTab
 * @package php\gui
 * @packages gui, javafx
 *
 * Events: close, closeRequest, change
 */
class UXTab
{
    /**
     * @var bool
     */
    public $closable;

    /**
     * @var bool
     */
    public $disabled;

    /**
     * @var bool
     */
    public $disable;

    /**
     * @var bool
     */
    public $selected;

    /**
     * @var string
     */
    public $id;

    /**
     * @var UXNode
     */
    public $content;

    /**
     * @var UXNode
     */
    public $graphic;

    /**
     * @var string
     */
    public $text;

    /**
     * @var string
     */
    public $style;

    /**
     * @var UXTooltip
     */
    public $tooltip;

    /**
     * @var mixed
     */
    public $userData;

    /**
     * @var bool
     */
    public $draggable;

    /**
     * @param string $event
     * @param callable $handler
     * @param string $group
     */
    public function on($event, callable $handler, $group = 'general')
    {
    }

    /**
     * @param string $event
     * @param string $group (optional)
     */
    public function off($event, $group)
    {
    }

    /**
     * @param string $event
     * @param UXEvent $e (optional)
     */
    public function trigger($event, UXEvent $e)
    {
    }

    /**
     * Getter and Setter for object data
     * @param string $name
     * @param mixed $value [optional]
     * @return mixed
     */
    public function data($name, $value)
    {
    }
}