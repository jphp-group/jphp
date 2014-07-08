<?php
namespace php\swing;

use php\swing\text\Style;

/**
 * Class UIRichTextArea
 * @package php\swing
 */
class UIRichTextArea extends UITextElement {
    /**
     * @var Style
     */
    public $logicalStyle;

    /**
     * @var string - ALWAYS, HIDDEN, AUTO
     */
    public $horScrollPolicy;

    /**
     * @var string - ALWAYS, HIDDEN, AUTO
     */
    public $verScrollPolicy;

    /**
     * @param string $name
     * @param Style $parent
     * @return Style
     */
    public function addStyle($name, Style $parent = null) { return new Style(); }

    /**
     * @param string $name
     * @return Style
     */
    public function getStyle($name) { return new Style(); }

    /**
     * @param string $text
     * @param Style $style
     */
    public function appendText($text, Style $style) { }
}
