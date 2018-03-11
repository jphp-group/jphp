<?php
namespace php\gui;

/**
 * Class UXComboBoxBase
 * @package php\gui
 * @packages gui, javafx
 *
 * @method show()
 * @method hide()
 * @method arm()
 * @method disarm()
 */
class UXComboBoxBase extends UXControl
{
    /**
     * @var bool
     */
    public $armed;

    /**
     * Редактируемый.
     * @var bool
     */
    public $editable;

    /**
     * Текст-подсказка.
     * @var string
     */
    public $promptText;

    /**
     * @readonly
     * @var bool
     */
    public $showing;

    /**
     * Значение.
     * @var mixed
     */
    public $value;

    /**
     * Текст.
     * @var string
     */
    public $text;

    /**
     * @var bool
     */
    public $popupVisible = false;

    /**
     * Показать меню-попап.
     */
    public function showPopup()
    {
    }

    /**
     * Скрыть меню-попап.
     */
    public function hidePopup()
    {
    }
}