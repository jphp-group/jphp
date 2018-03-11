<?php
namespace php\game;

use php\gui\UXCanvas;
use php\gui\UXImage;
use php\lang\IllegalArgumentException;

/**
 * Class UXSprite
 * @package php\game
 * @packages game, javafx
 */
class UXSprite
{
    /**
     * @var UXImage
     */
    public $image;

    /**
     * @readonly
     * @var double[] width + height
     */
    public $frameSize = [0, 0];

    /**
     * @var double
     */
    public $frameWidth = 0;

    /**
     * @var double
     */
    public $frameHeight = 0;

    /**
     * @readonly
     * @var int
     */
    public $frameCount = 0;

    /**
     * @var int
     */
    public $speed = 12;

    /**
     * @var bool
     */
    public $cycledAnimation = true;

    /**
     * @var null|string
     */
    public $currentAnimation = null;

    /**
     * @param UXSprite $origin
     */
    public function __construct(UXSprite $origin = null)
    {
    }

    /**
     * @param string $name
     * @param int[] $indexes
     */
    public function setAnimation($name, array $indexes)
    {
    }

    /**
     * @param string $animation
     * @param int $speed (optional)
     */
    public function play($animation, $speed = -1)
    {
    }

    /**
     * @param UXCanvas $canvas
     * @param $index
     */
    public function draw(UXCanvas $canvas, $index)
    {
    }

    /**
     * @param UXCanvas $canvas
     */
    public function drawNext(UXCanvas $canvas)
    {
    }

    /**
     * @param UXCanvas $canvas
     * @param int $now
     */
    public function drawByTime(UXCanvas $canvas, $now)
    {
    }

    /**
     * @param int $index
     * @return UXImage
     * @throws IllegalArgumentException
     */
    public function getFrameImage($index)
    {
    }
}