<?php
namespace php\swing;

/**
 * Events -
 *      caretUpdate
 *
 * Class UITextElement
 * @package php\swing
 */
abstract class UITextElement extends UIContainer {
    /**
     * @var string
     */
    public $text;

    /**
     * @var bool
     */
    public $readOnly;

    /**
     * @var int
     */
    public $caretPos;

    /**
     * @var Color
     */
    public $caretColor;

    /**
     * @var int
     */
    public $selectedStart;

    /**
     * @var int
     */
    public $selectedEnd;

    /**
     * @readonly
     * @var int
     */
    public $selectedText;

    /**
     * @var Color
     */
    public $selectionColor;

    /**
     * @var Color
     */
    public $selectionTextColor;

    /**
     * @var Color
     */
    public $disabledTextColor;

    /**
     * int[]
     * @var array [top, left, bottom, right]
     */
    public $margin;


    /** Methods */
    /**
     * Copy to clipboard
     */
    public function copy() { }

    /**
     * Cut to clipboard
     */
    public function cut() { }

    /**
     * Paste from clipboard
     */
    public function paste() { }

    /**
     * @param int $selStart
     * @param int $selEnd
     */
    public function select($selStart, $selEnd) { }

    /**
     * Select all text
     */
    public function selectAll() { }

    /**
     * @param string $content
     */
    public function replaceSelection($content) { }

    /**
     * A convenience print method that displays a print dialog, and then
     * prints this element in interactive mode with no
     * header or footer text. Note: this method
     * blocks until printing is done.
     * @return bool
     */
    public function printDialog() { }
}
