<?php
namespace php\swing;

/**
 * Class UIMenu
 * @package php\swing
 */
class UIMenu extends UIMenuItem {
    /**
     * @readonly
     * @var int
     */
    public $itemCount;

    /**
     * @var int
     */
    public $delay;

    /**
     * @var bool
     */
    public $popupVisible;

    /**
     * @param int $pos
     * @return UIMenuItem
     */
    public function getItem($pos) { }

    /**
     * Add separator
     */
    public function addSeparator() { }

    /**
     * @param int $x
     * @param int $y
     */
    public function setMenuPosition($x, $y) { }

    /**
     * @return bool
     */
    public function isTopLevelMenu() { }
}
