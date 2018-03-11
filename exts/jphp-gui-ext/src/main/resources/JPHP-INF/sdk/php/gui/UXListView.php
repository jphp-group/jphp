<?php
namespace php\gui;

/**
 * Class UXListView
 * @package php\gui
 * @packages gui, javafx
 */
class UXListView extends UXControl
{


    /**
     * Редактируемый.
     * @var bool
     */
    public $editable = false;

    /**
     * @readonly
     * @var int
     */
    public $editingIndex = -1;

    /**
     * Фиксированный размер (высота) строк.
     * @var double
     */
    public $fixedCellSize = -1;

    /**
     * @var UXNode
     */
    public $placeholder = null;

    /**
     * Список.
     * @var UXList
     */
    public $items;

    /**
     * Ориентация.
     * @var string HORIZONTAL or VERTICAL
     */
    public $orientation = 'HORIZONTAL';

    /**
     * Множественное выделение.
     * @var bool
     */
    public $multipleSelection = false;

    /**
     * Выделенные индексы (массив), начиная с 0.
     * @var int[]
     */
    public $selectedIndexes = [];

    /**
     * Выделенный индекс, начиная с 0.
     * @var int
     */
    public $selectedIndex = -1;

    /**
     * Сфокусированный индекс.
     * @var int
     */
    public $focusedIndex = -1;

    /**
     * Выделенные элементы.
     * @readonly
     * @var mixed[]
     */
    public $selectedItems = [];

    /**
     * Выделенный элемент.
     * @readonly
     * @var mixed
     */
    public $selectedItem = null;

    /**
     * Сфокусированный элемент.
     * @readonly
     * @var mixed
     */
    public $focusedItem = null;

    /**
     * @var string
     */
    public $itemsText;

    /**
     * Скролить к индексу.
     * @param int $index
     */
    public function scrollTo($index)
    {
    }

    /**
     * @param int $index
     */
    public function edit($index)
    {
    }

    /**
     * @param callable|null $handler  (UXListCell $cell, $item, $empty)
     */
    public function setCellFactory(callable $handler)
    {
    }

    /**
     * @param callable|null $handler (UXListCell $cell, $item, $empty)
     * @param callable|null $dragDoneHandler (UXDragEvent $e, UXListView $view)
     */
    public function setDraggableCellFactory(callable $handler, callable $dragDoneHandler)
    {
    }

    /**
     * Визуально обновить список.
     */
    public function update()
    {
    }
}