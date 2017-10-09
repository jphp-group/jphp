<?php
namespace php\android\view;
use php\android\app\Activity;

/**
 * Class View
 */
class View {

    /**
     * @param Activity $context
     */
    public function __construct(Activity $context) { }

    /**
     * @param string $event
     * @param callable $callback
     */
    public function on($event, callable $callback) { }

    /**
     * @param string $event
     */
    public function off($event) { }

    /**
     * @param string $event
     */
    public function trigger($event) { }
}