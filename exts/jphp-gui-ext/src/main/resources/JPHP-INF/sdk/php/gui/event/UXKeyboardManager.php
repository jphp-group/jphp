<?php
namespace php\gui\event;

use php\gui\UXWindow;

/**
 * Менеджер для отлова событий клавиатуры.
 *
 * Class UXKeyboardManager
 * @package php\gui\event
 * @packages gui, javafx
 */
class UXKeyboardManager
{
    /**
     * @param UXWindow $window
     */
    public function __construct(UXWindow $window)
    {
    }

    /**
     * Уничтожить менеджер
     */
    public function free()
    {
    }

    /**
     * @param string $keyCombination
     * @param callable $handler
     * @param string $group
     */
    public function onPress($keyCombination, callable $handler, $group = "general")
    {
    }

    /**
     * @param string $keyCombination
     * @param callable $handler
     * @param string $group
     */
    public function onDown($keyCombination, callable $handler, $group = "general")
    {
    }

    /**
     * @param string $keyCombination
     * @param callable $handler
     * @param string $group
     */
    public function onUp($keyCombination, callable $handler, $group = "general")
    {
    }
}