<?php
namespace android\widget;

use android\view\View;

/**
 * Class TextView
 * @package android\widget
 */
class TextView extends View {

    /**
     * @param string $text
     */
    public function setText($text) { }

    /**
     * @return string
     */
    public function getText() { }

    /**
     * @param int $type see android\text\InputType constants
     */
    public function setInputType($type) { }

    /**
     * @return int
     */
    public function getInputType() { }
}