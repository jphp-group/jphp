<?php
namespace php\gui;

use php\gui\layout\UXFlowPane;
use php\gui\paint\UXColor;
use php\gui\text\UXFont;

/**
 * Class UXPagination
 * @package php\gui
 * @packages gui, javafx
 */
class UXPagination extends UXFlowPane
{


    /**
     * @var int
     */
    public $total = 0;

    /**
     * @var int
     */
    public $pageSize = 20;

    /**
     * @readonly
     * @var int
     */
    public $pageCount;

    /**
     * @var int
     */
    public $maxPageCount = 7;

    /**
     * @var int
     */
    public $selectedPage = 0;

    /**
     * @var string
     */
    public $hintText = '';

    /**
     * @var bool
     */
    public $showTotal = false;

    /**
     * @var UXFont
     */
    public $font;

    /**
     * @var UXColor
     */
    public $textColor;

    /**
     * @var bool
     */
    public $showPrevNext = true;

    /**
     * @readonly
     * @var UXButton
     */
    public $previousButton;

    /**
     * @readonly
     * @var UXButton
     */
    public $nextButton;

    public function __construct()
    {
    }
}