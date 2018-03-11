<?php
namespace php\gui;

/**
 * Class UXCell
 * @package php\gui
 * @packages gui, javafx
 */
class UXCell extends UXLabeled
{


    /**
     * @var mixed
     */
    public $item;

    /**
     * @var bool
     */
    public $editable;

    /**
     * @readonly
     * @var bool
     */
    public $editing;

    /**
     * @readonly
     * @var bool
     */
    public $empty;

    /**
     * @var bool
     */
    public $selected;

    /**
     * @param bool $value
     */
    public function updateSelected($value)
    {
    }

    public function startEdit()
    {
    }

    public function cancelEdit()
    {
    }

    /**
     * @param mixed $value
     */
    public function commitEdit($value)
    {
    }
}