<?php
namespace php\game;

use ide\Logger;
use php\gui\UXData;
use php\gui\UXImage;
use php\gui\UXNodeWrapper;
use php\io\IOException;
use php\lang\IllegalStateException;

class UXGameBackgroundWrapper extends UXNodeWrapper
{
    public function applyData(UXData $data)
    {
        parent::applyData($data);

        $image = null;

        if (!$data->toArray()) {
            throw new IllegalStateException();
        }

        if ($data->get('image')) {
            try {
                $image = $this->node->image = new UXImage('res://' . $data->get('image'));
            } catch (\Exception $e) {
                ;
            }
        }

        $this->node->velocity = [(int) $data->get('velocityX'), (int) $data->get('velocityY')];
    }
}