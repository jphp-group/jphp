<?php
namespace php\game;

use php\gui\UXCanvas;
use php\gui\UXImage;

/**
 * Class UXGameBackground
 * @package php\game
 * @packages game, javafx
 */
class UXGameBackground extends UXCanvas
{
    /**
     * --RU--
     * Изображение.
     * @var UXImage
     */
    public $image;

    /**
     * --RU--
     * Линейная скорость [x, y].
     * @var array
     */
    public $velocity = [0, 0];

    /**
     * --RU--
     * Позиция вида [x, y].
     * @var array
     */
    public $viewPosition = [0, 0];

    /**
     * --RU--
     * Авторазмер.
     * @var bool
     */
    public $autoSize = false;

    /**
     * --RU--
     * Отразить изображение по X.
     * @var bool
     */
    public $flipX = false;

    /**
     * --RU--
     * Отразить изображение по Y.
     * @var bool
     */
    public $flipY = false;
}