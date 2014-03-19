<?php
namespace php\swing;

/**
 * Class UITable
 * @package php\swing
 *
 *      TODO: finish!
 */
class UITable extends UIContainer {
    /**
     * @var bool
     */
    public $dragEnabled;

    /**
     * @var Color
     */
    public $selectionBackground;

    /**
     * @var Color
     */
    public $selectionForeground;

    /**
     * @var Color
     */
    public $gridColor;

    /**
     * @var int
     */
    public $editingColumn;

    /**
     * @var int
     */
    public $editingRow;

    /**
     * @var int
     */
    public $rowMargin;

    /**
     * @param int $height
     * @param null|int $row
     */
    public function setRowHeight($height, $row = null) { }

    /**
     * @param null|int $row
     * @return int
     */
    public function getRowHeight($row = null) { }

    /**
     * @param string|null $value
     * @param int $row
     * @param int $column
     */
    public function setValueAt($value, $row, $column) { }

    /**
     * @param int $row
     * @param int $column
     * @return string|null
     */
    public function getValueAt($row, $column) { }

    /**
     * @param int $x
     * @param int $y
     * @return int
     */
    public function columnAtPoint($x, $y) { }

    /**
     * @param int $x
     * @param int $y
     * @return int
     */
    public function rowAtPoint($x, $y) { }

    /**
     * @param int $row
     * @param int $column
     * @return bool
     */
    public function editCellAt($row, $column) { }

    /**
     * @param int $index0
     * @param int $index1
     */
    public function addColumnSelectionInterval($index0, $index1) { }

    /**
     * @param int $index0
     * @param int $index1
     */
    public function addRowSelectionInterval($index0, $index1) { }

    /**
     * @param int $column
     * @return string
     */
    public function getColumnName($column) { }

    /**
     * @param int $column
     */
    public function setEditingColumn($column) { }

    /**
     * @param int $row
     */
    public function setEditingRow($row) { }
}
