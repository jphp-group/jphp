<?php
namespace ext\javafx\event;

/**
 * Class UXKeyEvent
 * @package ext\javafx\event
 */
class UXKeyEvent extends UXEvent
{
    /**
     * @var string
     */
    public $character;

    /**
     * @var string
     */
    public $text;

    /**
     * @var int
     */
    public $code;

    /**
     * @var bool
     */
    public $altDown;

    /**
     * @var bool
     */
    public $controlDown;

    /**
     * @var bool
     */
    public $shiftDown;

    /**
     * @var bool
     */
    public $metaDown;

    /**
     * @var bool
     */
    public $shortcutDown;
}