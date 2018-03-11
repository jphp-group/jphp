<?php
namespace php\gui;

/**
 * Class UXTitledPane
 * @package php\gui
 * @packages gui, javafx
 */
class UXTitledPane extends UXLabeled
{
    /**
     * @var UXNode
     */
    public $content;

    /**
     * @var bool
     */
    public $animated;

    /**
     * @var bool
     */
    public $collapsible;

    /**
     * @var bool
     */
    public $expanded;

    /**
     * @param string $title (optional)
     * @param UXNode $content
     */
    public function __construct($title, UXNode $content = null)
    {
    }
}