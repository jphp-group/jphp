<?php
namespace php\gui;

/**
 * @package php\gui
 * @packages gui, javafx
 */
class UXNumberSpinner extends UXSpinner
{
    /**
     * @var int
     */
    public $min = 0x80000000;

    /**
     * @var int
     */
    public $max = 0x7fffffff;

    /**
     * @var int
     */
    public $step = 1;

    /**
     * @var int
     */
    public $initial = 0;

    /**
     * @var int
     */
    public $value = 0;

    /**
     * RIGHT_VERTICAL, RIGHT_HORIZONTAL, LEFT_VERTICAL, LEFT_HORIZONTAL, SPLIT_VERTICAL, SPLIT_HORIZONTAL
     *
     * @var string
     */
    public $arrowsStyle = 'RIGHT_VERTICAL';
}