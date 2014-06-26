<?php
namespace php\swing;

/**
 * Class UIListbox
 * @package php\swing
 */
class UIListbox extends UIContainer {
    /**
     * @var bool
     */
    public $multiple;

    /**
     * @var int
     */
    public $selectedIndex;

    /**
     * @var int[]
     */
    public $selectedIndexes;

    /**
     * @var int
     */
    public $maxSelectionIndex;

    /**
     * @var int
     */
    public $minSelectionIndex;

    /**
     * @var int
     */
    public $visibleRowCount;

    /**
     * @var Color
     */
    public $selectionBackground;

    /**
     * @var Color
     */
    public $selectionForeground;

    /**
     * @var string - ALWAYS, HIDDEN, AUTO
     */
    public $horScrollPolicy;

    /**
     * @var string - ALWAYS, HIDDEN, AUTO
     */
    public $verScrollPolicy;

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

    /**
     * @param callable $callback (UIListbox $self, UILabel $template, $value, int $index, bool isSelected, bool cellHasFocus)
     */
    public function onCellRender(callable $callback = null) { }
}
