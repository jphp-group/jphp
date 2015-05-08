<?php
namespace ext\javafx;

/**
 * Class UXCheckbox
 * @package ext\javafx
 */
class UXCheckbox extends UXButtonBase
{
    /**
     * @var bool
     */
    public $selected;

    /**
     * @var bool
     */
    public $indeterminate;

    /**
     * @var bool
     */
    public $allowIndeterminate;

    /**
     * @param string $text (optional)
     */
    public function __construct($text) {}
}