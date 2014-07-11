<?php
namespace php\swing;

/**
 * Class UIPopupMenu
 *
 * Events: open, close, cancel
 *
 * @package php\swing
 */
class UIPopupMenu extends UIContainer {
    /**
     * @var bool
     */
    public $borderPainted;

    /**
     * @var bool
     */
    public $lightweightPopup;

    /**
     * @var string
     */
    public $label;

    /**
     * Add separator
     */
    public function addSeparator() { }

    /**
     * @param int $width
     * @param int $height
     */
    public function setPopupSize($width, $height) { }

    /**
     * @param UIElement $component
     */
    public function setSelected(UIElement $component) { }
}
