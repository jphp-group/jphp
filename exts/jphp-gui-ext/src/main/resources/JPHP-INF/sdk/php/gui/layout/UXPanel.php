<?php
namespace php\gui\layout;

use php\gui\paint\UXColor;
use php\gui\text\UXFont;

/**
 * Class UXPanel
 * @package php\gui\layout
 * @packages gui, javafx
 */
class UXPanel extends UXAnchorPane
{
    /**
     * Заголовок
     * @var string
     */
    public $title;

    /**
     * Цвет заголовка.
     * @var UXColor
     */
    public $titleColor;

    /**
     * Шрифт заголовка.
     * @var UXFont
     */
    public $titleFont;

    /**
     * Позиция заголовка TOP_LEFT, TOP_RIGHT, TOP_CENTER.
     * @var string
     */
    public $titlePosition = 'TOP_LEFT';

    /**
     * Смещение заголовка.
     * @var int
     */
    public $titleOffset = 15;

    /**
     * @var int
     */
    public $borderWidth = 1;

    /**
     * @var UXColor
     */
    public $borderColor;

    /**
     * @var int
     */
    public $borderRadius = 0;

    /**
     * @var string SOLID, DOTTED, DASHED, NONE
     */
    public $borderStyle = 'SOLID';
}