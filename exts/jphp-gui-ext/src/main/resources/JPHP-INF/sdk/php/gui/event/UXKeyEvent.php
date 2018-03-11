<?php
namespace php\gui\event;

/**
 * Class UXKeyEvent
 * @package php\gui\event
 * @packages gui, javafx
 */
class UXKeyEvent extends UXEvent
{
    /**
     * @var string
     */
    public $character;

    /**
     * @var string
     */
    public $text;

    /**
     * @var string
     */
    public $codeName;

    /**
     * @var bool
     */
    public $altDown;

    /**
     * @var bool
     */
    public $controlDown;

    /**
     * @var bool
     */
    public $shiftDown;

    /**
     * @var bool
     */
    public $metaDown;

    /**
     * @var bool
     */
    public $shortcutDown;

    /**
     * @param UXKeyEvent $parent
     * @param $sender
     */
    public function __construct(UXKeyEvent $parent, $sender)
    {
    }

    /**
     * @param string $accelerator e.g. Control + R
     * @return bool
     */
    public function matches($accelerator)
    {
    }

    /**
     * Left, right, up, down keys (including the keypad arrows)
     * @return bool
     */
    public function isArrowKey()
    {
    }

    /**
     * All Digit keys (including the keypad digits)
     * @return bool
     */
    public function isDigitKey()
    {
    }

    /**
     * Function keys like F1, F2, etc...
     * @return bool
     */
    public function isFunctionKey()
    {
    }

    /**
     * Navigation keys are arrow keys and Page Down, Page Up, Home, End
     * (including keypad keys).
     *
     * @return bool
     */
    public function isNavigationKey()
    {
    }

    /**
     * Keys that could act as a modifier.
     * @return bool
     */
    public function isModifierKey()
    {
    }

    /**
     * All keys with letters.
     * @return bool
     */
    public function isLetterKey()
    {
    }

    /**
     * All keys on the keypad.
     * @return bool
     */
    public function isKeypadKey()
    {
    }

    /**
     * Space, tab and enter.
     * @return bool
     */
    public function isWhitespaceKey()
    {
    }

    /**
     * All multimedia keys (channel up/down, volume control, etc...).
     * @return bool
     */
    public function isMediaKey()
    {
    }

    /**
     * This value is used to indicate that the keyCode is unknown.
     * Key typed events do not have a keyCode value; this value
     * is used instead.
     *
     * @return bool
     */
    public function isUndefinedKey()
    {
    }
}