<?php
namespace php\swing;

/**
 * Class UIProgress
 * @package php\swing
 */
class UIProgress extends UIContainer {
    /**
     * @var int
     */
    public $value;

    /**
     * @var int
     */
    public $max;

    /**
     * @var int
     */
    public $min;

    /**
     * @var string
     */
    public $text;

    /**
     * @var bool
     */
    public $textPainted;

    /**
     * @var bool
     */
    public $borderPainted;
}
