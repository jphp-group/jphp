<?php
namespace php\game;

use game\SpriteManager;
use game\SpriteSpec;
use ide\formats\sprite\IdeSpriteManager;
use php\gui\UXData;
use php\gui\UXNodeWrapper;

class UXSpriteViewWrapper extends UXNodeWrapper
{
    public function applyData(UXData $data)
    {
        parent::applyData($data);

        if ($sprite = $data->get('sprite')) {
            SpriteSpec::apply($sprite, $this->node);

            if ($data->get('animated')) {
                $this->node->sprite->unfreeze();
            } else {
                $this->node->sprite->freeze();
            }
        }

        $this->node->animated = true;
    }
}