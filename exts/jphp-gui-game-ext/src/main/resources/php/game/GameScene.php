<?php
namespace php\game;

use php\gui\framework\AbstractForm;
use php\gui\UXCustomNode;
use php\lang\IllegalStateException;

/**
 * Class AbstractScene
 * @package php\game
 */
class GameScene extends AbstractForm
{
    const DEFAULT_PATH = 'res://.game/scenes/';

    protected $name;

    /**
     * @param string $name
     */
    public function __construct($name)
    {
        parent::__construct(null, false);

        $this->name = $name;
    }

    protected function loadCustomNode(UXCustomNode $node)
    {
        $type = $node->get('type');
        $x    = $node->get('x');
        $y    = $node->get('y');

        $this->create($type, null, $x, $y);
    }

    public function getName()
    {
        return $this->name;
    }

    protected function getResourceName()
    {
        return $this->name;
    }
}