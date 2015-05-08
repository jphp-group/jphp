<?php
namespace ext\javafx\event;
use ext\javafx\UXNode;

/**
 * Class Event
 * @package ext\javafx\event
 */
abstract class UXEvent
{
    /**
     * @var object|UXNode
     */
    public $target;

    /**
     * @return bool
     */
    public function isConsumed() {}

    /**
     * ...
     */
    public function consume() {}
}