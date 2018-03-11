<?php
namespace php\game;

use php\gui\UXCanvas;

/**
 * Class UXSpriteView
 * @package php\game
 * @packages game, javafx
 */
class UXSpriteView extends UXCanvas
{
    /**
     * Спрайт.
     * @var UXSprite
     */
    public $sprite = null;

    /**
     * Анимирован.
     * @var bool
     */
    public $animated = false;

    /**
     * Анимация.
     * @var null|string
     */
    public $animationName = null;

    /**
     * Скорость анимации.
     * @var int
     */
    public $animationSpeed = 12;

    /**
     * Кадр спрайта.
     * @var int
     */
    public $frame = -1;

    /**
     * Количество кадров.
     * @readonly
     * @var int
     */
    public $frameCount = 0;

    /**
     * Отразить спрайт по X.
     * @var bool
     */
    public $flipX = false;

    /**
     * Отразить спрайт по Y.
     * @var bool
     */
    public $flipY = false;

    /**
     * UXSpriteView constructor.
     * @param UXSprite $sprite
     */
    public function __construct(UXSprite $sprite = null)
    {
    }

    /**
     * Проигрывать анимацию циклично.
     * @param string $animation
     * @param int $speed (optional)
     */
    public function play($animation, $speed = -1)
    {
    }

    /**
     * Проиграть анимацию единожды.
     * @param string $animation
     * @param int $speed
     */
    public function playOnce($animation, $speed = -1)
    {
    }

    public function pause()
    {
    }

    public function resume()
    {
    }

    /**
     * @return bool
     */
    public function isPaused()
    {
    }
}