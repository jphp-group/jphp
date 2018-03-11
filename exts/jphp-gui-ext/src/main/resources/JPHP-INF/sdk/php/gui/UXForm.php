<?php
namespace php\gui;


/**
 * @package php\gui
 *
 * @packages gui, javafx
 *
 * @property double $maxHeight
 * @property double $maxWidth
 * @property double $minHeight
 * @property double $minWidth
 *
 * @property bool $fullScreen
 * @property bool $iconified
 * @property bool $resizable
 */
class UXForm extends UXWindow
{


    /**
     * --RU--
     * Заголовок окна.
     * @var string
     */
    public $title;

    /**
     * NONE, WINDOW_MODAL, APPLICATION_MODAL
     * @var string
     */
    public $modality;

    /**
     * --RU--
     * Всегда поверх всех окон.
     *
     * @var bool
     */
    public $alwaysOnTop;

    /**
     * --RU--
     * Раскрытость на весь экран.
     *
     * @var bool
     */
    public $maximized;

    /**
     * @var UXWindow
     */
    public $owner;

    /**
     * --RU--
     * Стиль окна.
     *
     * DECORATED, UNDECORATED, TRANSPARENT, UTILITY, UNIFIED
     * @var string
     */
    public $style;

    /**
     * --RU--
     * Иконки окна.
     * @var UXList of UXImage
     */
    public $icons;

    /**
     * --RU--
     * Прозрачность.
     * @var bool
     */
    public $transparent = false;

    /**
     * @var bool
     */
    public $resizable = true;

    /**
     * @var bool
     */
    public $iconified = false;

    /**
     * --RU--
     * Фулскрин.
     * @var bool
     */
    public $fullScreen = false;

    /**
     * @param UXForm $form [optional]
     */
    public function __construct(UXForm $form)
    {
    }

    /**
     * --RU--
     * Показать окно и ожидать его закрытия.
     * ! Только для модальных окон.
     */
    public function showAndWait()
    {
    }

    /**
     * --RU--
     * Переместить назад (под все окна).
     */
    public function toBack()
    {
    }

    /**
     * --RU--
     * Переместить вперед (над всеми окнами).
     */
    public function toFront()
    {
    }

    public function maximize()
    {
    }
}