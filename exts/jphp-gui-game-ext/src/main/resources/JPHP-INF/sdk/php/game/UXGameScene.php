<?php
namespace php\game;
use php\gui\layout\UXAnchorPane;

/**
 * Class UXGameScene
 * @package php\game
 * @packages game, javafx
 */
class UXGameScene
{
    /**
     * @var array [x, y]
     */
    public $gravity = [0.0, 0.0];

    /**
     * @var float
     */
    public $gravityX = 0.0;

    /**
     * @var float
     */
    public $gravityY = 0.0;

    /**
     * @var UXGameEntity|null
     */
    public $observedObject = null;

    /**
     * UXGameScene constructor.
     */
    public function __construct()
    {
    }

    /**
     * @param UXGameEntity $entity
     */
    public function add(UXGameEntity $entity)
    {
    }

    /**
     * @param UXGameEntity $entity
     */
    public function remove(UXGameEntity $entity)
    {
    }

    public function play()
    {
    }

    public function pause()
    {
    }

    public function clear()
    {
    }

    /**
     * @param callable|null $handler (x, y)
     */
    public function setScrollHandler(callable $handler)
    {
    }
}