<?php
namespace php\gui;
use php\gui\text\UXFont;

/**
 * Class UXTextInputControl
 * @package php\gui
 * @packages gui, javafx
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
     * @var UXFont
     */
    public $font;

    /**
     * array(start, end, length).
     * @var array
     */
    public $selection;

    /**
     * @var string
     */
    public $selectedText;

    /**
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

    /**
     * @var int
     */
    public $caretPosition;

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

    /**
     * Deselect all.
     */
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

    /**
     * Undo changes.
     */
    public function undo()
    {
    }

    /**
     * Redo changes.
     */
    public function redo()
    {
    }

    /**
     * Commit the current text and convert it to a value.
     */
    public function commitValue()
    {
    }

    /**
     * If the field is currently being edited, this call will set text to the last commited value.
     */
    public function cancelEdit()
    {
    }
}