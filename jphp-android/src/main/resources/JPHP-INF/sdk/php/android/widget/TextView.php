<?php
namespace php\android\widget;

use php\android\view\View;

/**
 * Class TextView
 * @package php\android\widget
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
     * @param int $type see php\android\text\InputType constants
     */
    public function setInputType($type) { }

    /**
     * @return int
     */
    public function getInputType() { }
}