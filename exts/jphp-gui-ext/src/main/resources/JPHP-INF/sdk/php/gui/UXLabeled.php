<?php
namespace php\gui;
use php\gui\paint\UXColor;
use php\gui\text\UXFont;

/**
 * Class UXLabeled
 * @package php\gui
 *
 * @packages gui, javafx
 */
abstract class UXLabeled extends UXControl
{


    /**
     * Выравнивание для контейнеров.
     * @var string
     */
    public $alignment = 'BASELINE_CENTER';

    /**
     * Текстовое выравнивание.
     * @var string
     */
    public  $textAlignment = 'LEFT';

    /**
     * @var bool
     */
    public $wrapText = false;

    /**
     * Подчеркивание.
     * @var bool
     */
    public $underline = false;

    /**
     * Текст.
     * @var string
     */
    public $text;

    /**
     * Шрифт.
     * @var UXFont
     */
    public $font;

    /**
     * Иконка.
     * @var UXNode
     */
    public $graphic;

    /**
     * Отступ для иконки.
     * @var double
     */
    public $graphicTextGap = 4;

    /**
     * Цвет текста.
     * @var UXColor
     */
    public $textColor;

    /**
     * Текст сокращения.
     * @var string
     */
    public $ellipsisString = '...';

    /**
     * Отображение контента.
     * @var string LEFT, TOP, RIGHT, BOTTOM, TEXT_ONLY, GRAPHIC_ONLY
     */
    public $contentDisplay = 'LEFT';

    /**
     * @var bool
     */
    public $mnemonicParsing = false;
}