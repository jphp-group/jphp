<?php
namespace php\gui;

/**
 * Class UXTreeItem
 * @package php\gui
 * @packages gui, javafx
 */
class UXTreeItem
{
    /**
     * @var mixed
     */
    public $value;

    /**
     * @var bool
     */
    public $expanded = false;

    /**
     * @var UXNode
     */
    public $graphic;

    /**
     * @readonly
     * @var UXTreeItem
     */
    public $parent;

    /**
     * @readonly
     * @var UXList of UXTreeItem
     */
    public $children;

    /**
     * @param mixed $value
     */
    public function __construct($value = null)
    {
    }

    /**
     * Update tree item.
     */
    public function update()
    {
    }
}