<?php
namespace php\gui;

/**
 * Class UXLabelEx
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXLabelEx extends UXLabel
{


    /**
     * @var UXNode
     */
    public $autoSize = false;

    /**
     * @var string ALL, HORIZONTAL, VERTICAL
     */
    public $autoSizeType = 'ALL';
}