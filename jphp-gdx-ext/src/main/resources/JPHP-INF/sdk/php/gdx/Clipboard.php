<?php
namespace php\gdx;

/**
 * Class Clipboard
 * @package php\gdx
 */
class Clipboard {
    private function __construct() { }

    /**
     * gets the current content of the clipboard if it contains text
     * @return string the clipboard content or null
     */
    public function getContent() { }

    /**
     * Sets the content of the system clipboard.
     *
     * @param string $content
     */
    public function setContent($content) { }
}