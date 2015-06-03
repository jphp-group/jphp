<?php
namespace php\swing;

class UILabel extends UIContainer {
    /**
     * Text of label
     * @var string
     */
    public $text;

    /**
     * Direction
     * @var int
     */
    public $verPosition;

    /**
     * Direction
     * @var int
     */
    public $horPosition;

    /**
     * Direction
     * @var int
     */
    public $verAlignment;

    /**
     * Direction
     * @var int|string
     */
    public $horAlignment;

    /**
     * @var int
     */
    public $iconTextGap;

    /**
     * @param UIElement $component
     */
    public function setLabelFor(UIElement $component) { }

    /**
     * @param Image|string $icon - filename or Image
     */
    public function setIcon($icon) { }

    /**
     * @param Image|string $icon
     */
    public function setDisabledIcon($icon) { }
}
