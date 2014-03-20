<?php
namespace php\swing;

class UICombobox extends UIContainer {

    /**
     * @var bool
     */
    public $readOnly;

    /**
     * @var bool
     */
    public $popupVisible;

    /**
     * @var bool
     */
    public $lightweightPopup;

    /**
     * @readonly
     * @var int
     */
    public $itemCount;

    /**
     * @var int
     */
    public $selectedIndex;

    /**
     * @var int
     */
    public $maxRowCount;

    /**
     * @param array $items
     */
    public function setItems(array $items) { }

    /**
     * @param int $index
     * @return string|null
     */
    public function getItem($index) { }

    /**
     * @param string $item
     */
    public function addItem($item) { }

    /**
     * @param int $index
     * @param string $item
     */
    public function insertItem($index, $item) { }

    /**
     * @param int $index
     */
    public function removeItem($index) { }

    /**
     * clear all
     */
    public function removeAllItems() { }
}