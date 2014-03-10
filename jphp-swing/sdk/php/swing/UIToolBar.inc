<?php
namespace php\swing;

/**
 * Class UIToolBar
 * @package php\swing
 */
class UIToolBar extends UIContainer {
    /**
     * @var bool
     */
    public $vertical;

    /**
     * @var bool
     */
    public $borderPainted;

    /**
     * @var bool
     */
    public $rollover;

    /**
     * @var bool
     */
    public $floatable;

    /**
     * @param null|int $w
     * @param null|int $h
     */
    public function addSeparator($w = null, $h = null) { }
}
