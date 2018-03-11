<?php
namespace php\game;

use php\gui\UXNode;
use php\gui\UXParent;

/**
 * Class UXGameObject
 * @package php\game
 * @packages game, javafx
 */
class UXGameEntity
{
    /**
     * @var UXNode
     */
    public $node;

    /**
     * @var bool
     */
    public $active = true;

    /**
     * @var float
     */
    public $x;

    /**
     * @var float
     */
    public $y;

    /**
     * @var float
     */
    public $centerX;

    /**
     * @var float
     */
    public $centerY;

    /**
     * @var string STATIC, DYNAMIC, KINEMATIC
     */
    public $bodyType = 'STATIC';

    /**
     * If null, using scene gravity
     * @var array|null [x, y]
     */
    public $gravity = null;

    /**
     * @var float
     */
    public $gravityX = 0.0;

    /**
     * @var float
     */
    public $gravityY = 0.0;

    /**
     * @readonly
     * @var UXGameScene
     */
    public $gameScene = null;

    /**
     * @var array
     */
    public $velocity = [0, 0];

    /**
     * @var float
     */
    public $velocityX = 0.0;

    /**
     * @var float
     */
    public $velocityY = 0.0;

    /**
     * @var array alias of velocity property
     */
    public $angleSpeed = [0, 0];

    /**
     * @var float angle speed
     */
    public $speed = 0.0;

    /**
     * @var float angle for speed, from 0 to 360
     */
    public $direction = 0.0;

    /**
     * @var float alias of velocityX
     */
    public $hspeed = 0.0;

    /**
     * @var float alias of velocityY
     */
    public $vspeed = 0.0;

    /**
     * @var string NONE, PLATFORM, PHYSIC
     */
    public $solidType = 'NONE';

    /**
     * @readonly
     * @var bool
     */
    public $solid = false;

    /**
     * @readonly
     * @var bool
     */
    public $platform = false;

    /**
     * @param string $entityType
     * @param UXNode $node
     */
    public function __construct($entityType, UXNode $node)
    {
    }

    /**
     * @param array $points  [[x,y], [x,y], [x,y], [x,y] ...]
     */
    public function setPolygonFixture(array $points)
    {
    }

    /**
     * @param float $width
     * @param float $height
     */
    public function setRectangleFixture($width, $height)
    {
    }

    /**
     * @param float $width
     * @param float $height
     */
    public function setEllipseFixture($width, $height)
    {
    }

    /**
     * @param float $radius
     */
    public function setCircleFixture($radius)
    {
    }

    /**
     * @param string $entityType
     * @param callable|null $handler
     */
    public function setCollisionHandler($entityType, callable $handler)
    {
    }
}