<?php
namespace php\gui\framework\event;
use php\gui\UXNode;
use behaviour\custom\GameEntityBehaviour;
use php\lib\items;

/**
 * Class CollisionEventAdapter
 * @package php\gui\framework\event
 */
class CollisionEventAdapter extends AbstractEventAdapter
{
    /**
     * @param $node
     * @param callable $handler
     * @param string $param
     * @return callable
     */
    public function adapt($node, callable $handler, $param)
    {
        /** @var UXNode $node */
        $entity = GameEntityBehaviour::get($node);

        $factoryName = $node->data('-factory-name');

        if ($entity) {
            $entity->setCollisionHandler($param, $handler, $factoryName);
        } else {
            $collisionHandlers = $node->data(__CLASS__);
            $collisionHandlers[$factoryName ? "$factoryName.$param" : $param] = $handler;

            $node->data(__CLASS__, $collisionHandlers);
        }

        return true;
    }
}