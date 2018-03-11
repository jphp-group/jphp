<?php
namespace php\gui;
use php\gui\paint\UXColor;
use php\gui\text\UXFont;

/**
 * Class UXImageArea
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXImageArea extends UXCanvas
{


    /**
     * Центроровано.
     * @var bool
     */
    public $centered = false;

    /**
     * Растягивать.
     * @var bool
     */
    public $stretch = false;

    /**
     * Растягивать по-умному, не растягивая маленькие картинки.
     * @var bool
     */
    public $smartStretch = false;

    /**
     * Авторазмер.
     * @var bool
     */
    public $autoSize = false;

    /**
     * Пропорциональность.
     * @var bool
     */
    public $proportional = false;

    /**
     * Мозаичное изображение.
     * @var bool
     */
    public $mosaic = false;

    /**
     * Отступы для мозайки.
     * @var float
     */
    public $mosaicGap = 0.0;

    /**
     * Текст.
     * @var string
     */
    public $text = '';

    /**
     * Шрифт текста.
     * @var UXFont
     */
    public $font;

    /**
     * Цвет текста.
     * @var UXColor
     */
    public $textColor;

    /**
     * Фоновый цвет.
     * @var UXColor
     */
    public $backgroundColor = null;

    /**
     * Изображение.
     * @var UXImage
     */
    public $image = null;

    /**
     * Изображение при наведении.
     * @var UXImage
     */
    public $hoverImage = null;

    /**
     * Изображение при клике.
     * @var UXImage
     */
    public $clickImage = null;

    /**
     * Отразить изображение по оси X.
     * @var bool
     */
    public $flipX = false;

    /**
     * Отразить изображение по оси Y.
     * @var bool
     */
    public $flipY = false;

    /**
     * @param UXImage|null $image
     */
    public function __construct(UXImage $image = null)
    {
    }
}