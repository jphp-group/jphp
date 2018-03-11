<?php
namespace php\gui\layout;

use php\gui\paint\UXColor;
use php\gui\UXParent;

/**
 * Class UXRegion
 * @package php\gui\layout
 * @packages gui, javafx
 */
class UXRegion extends UXParent
{
    /**
     * Минимальные размеры (ширина, высота)
     * @var double[]
     */
    public $minSize = [-1, -1];

    /**
     * Максимальные размеры (ширина, высота)
     * @var double[]
     */
    public $maxSize = [-1, -1];

    /**
     * Минимальная ширина.
     * @var double
     */
    public $minWidth = -1;

    /**
     * Минимальная высота.
     * @var double
     */
    public $minHeight = -1;

    /**
     * Максимальная ширина.
     * @var double
     */
    public $maxWidth = -1;

    /**
     * Максимальная высота
     * @var double
     */
    public $maxHeight = -1;

    /**
     * Предпочитаемые размеры.
     * @var array
     */
    public $prefSize = [0.0, 0.0];

    /**
     * @var float
     */
    public $prefWidth = 0.0;

    /**
     * @var float
     */
    public $prefHeight = 0.0;

    /**
     * Внутренние отступы.
     * @var array|double
     */
    public $padding = [0, 0, 0, 0];

    /**
     * @var double
     */
    public $paddingLeft = 0, $paddingTop = 0, $paddingRight = 0, $paddingBottom = 0;

    /**
     * Фоновый цвет.
     * @var UXColor
     */
    public $backgroundColor = null;
}