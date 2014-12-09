<?php
namespace php\swing;

/**
 * Class UIAbstractIButton
 * @package php\swing
 */
class UIAbstractIButton extends UIContainer {
    /**
     * Text of button
     * @var string
     */
    public $text;

    /**
     * @var bool
     */
    public $selected;

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
     * @var bool
     */
    public $borderPainted;

    /**
     * @var bool
     */
    public $focusPainted;

    /**
     * @var bool
     */
    public $rolloverEnabled;

    /**
     * @var bool
     */
    public $contentAreaFilled;

    /**
     * @var string
     */
    public $buttonGroup;

    /**
     * @param Image|string $icon - filename or Image
     */
    public function setIcon($icon) { }

    /**
     * @param Image|string $icon
     */
    public function setDisabledIcon($icon) { }

    /**
     * @param Image|string $icon
     */
    public function setSelectedIcon($icon) { }

    /**
     * @param Image|string $icon
     */
    public function setPressedIcon($icon) { }

    /**
     * @param Image|string $icon
     */
    public function setRolloverIcon($icon) { }

    /**
     * @param Image|string $icon
     */
    public function setDisabledSelectedIcon($icon) { }

    /**
     * @param Image|string $icon
     */
    public function setRolloverSelectedIcon($icon) { }

    /**
     * @param int $pressTime the time to "hold down" the button, in milliseconds
     */
    public function doClick($pressTime = 68) { }

    /**
     * @param string $buttonGroup
     * @return UIAbstractIButton[]
     */
    public static function getButtons($buttonGroup) { }

    /**
     * @param string $buttonGroup
     * @return UIAbstractIButton[]
     */
    public static function getSelectedButtons($buttonGroup) { }
}
