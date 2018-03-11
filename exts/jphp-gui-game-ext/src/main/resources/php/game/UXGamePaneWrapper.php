<?php
namespace php\game;

use php\gui\UXData;
use php\gui\UXNodeWrapper;
use php\gui\UXScrollPaneWrapper;

class UXGamePaneWrapper extends UXNodeWrapper
{
    public function applyData(UXData $data)
    {
        parent::applyData($data);

        /** @var UXGamePane $node */
        $node = $this->node;

        $handle = function () use ($node) {
            $node->content->data('--scroll-pane', $node);
            $node->content->data('--view-pane', $node);
        };

        $this->node->observer('content')->addListener($handle);

        $this->node->observer('viewX')->addListener(function ($old, $new) use ($node) {
            $node->content->data('--view-offset-x', $new);
        });

        $this->node->observer('viewY')->addListener(function ($old, $new) use ($node) {
            $node->content->data('--view-offset-y', $new);
        });
    }

}