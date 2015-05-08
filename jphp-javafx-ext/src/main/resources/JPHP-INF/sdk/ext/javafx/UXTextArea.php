<?php
namespace ext\javafx;

/**
 * Class UXTextArea
 * @package ext\javafx
 */
class UXTextArea extends UXTextInputControl
{
    /**
     * @var bool
     */
    public $wrapText;

    /**
     * @var double
     */
    public $scrollLeft;

    /**
     * @var double
     */
    public $scrollTop;

    /**
     * @var int
     */
    public $prefRowCount;

    /**
     * @var int
     */
    public $prefColumnCount;

    /**
     * @param string $text (optional)
     */
    public function __construct($text) {}
}