<?php
namespace ext\javafx;

/**
 * Class UXTextInputControl
 * @package ext\javafx
 */
abstract class UXTextInputControl extends UXControl
{
    /**
     * @var int
     */
    public $anchor;

    /**
     * @var string
     */
    public $text;

    /**
     * @readonly
     * @var string
     */
    public $selectedText;

    /**
     * @readonly
     * @var string
     */
    public $promptText;

    /**
     * @readonly
     * @var int
     */
    public $length;

    /**
     * @var bool
     */
    public $editable;

    public function copy() {}
    public function cut() {}
    public function paste() {}
    public function clear() {}

    public function end() {}
    public function home() {}
    public function forward() {}
    public function backward() {}
    public function nextWord() {}
    public function previousWord() {}

    public function selectAll() {}
    public function selectBackward() {}
    public function selectEnd() {}
    public function selectEndOfNextWord() {}
    public function selectForward() {}
    public function selectHome() {}
    public function selectNextWord() {}
    public function selectPreviousWord() {}

    /**
     * @param int $pos
     */
    public function selectPositionCaret($pos) {}

    /**
     * @param int $anchor
     * @param int $caretPosition
     */
    public function selectRange($anchor, $caretPosition) {}

    /**
     * @param int $pos
     */
    public function extendSelection($pos) {}

    public function deselect() {}

    /**
     * @param string $text
     */
    public function appendText($text) {}

    /**
     * @param int $index
     * @param string $text
     */
    public function insertText($index, $text) {}

    /**
     * @param int $start
     * @param int $end
     * @param string $text
     */
    public function replaceText($start, $end, $text) {}

    /**
     * @param string $text
     */
    public function replaceSelection($text) {}

    /**
     * @param int $pos
     */
    public function positionCaret($pos) {}
}