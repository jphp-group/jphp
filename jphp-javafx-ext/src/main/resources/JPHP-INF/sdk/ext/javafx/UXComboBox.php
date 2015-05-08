<?php
namespace ext\javafx;

use Traversable;

/**
 * Class UXComboBox
 * @package ext\javafx
 */
class UXComboBox extends UXComboBoxBase
{
    /**
     * @var UXList
     */
    public $items;

    /**
     * @var int
     */
    public $visibleRowCount;

    /**
     * @param array|Traversable $items (optional)
     */
    public function __construct($items) {}
}