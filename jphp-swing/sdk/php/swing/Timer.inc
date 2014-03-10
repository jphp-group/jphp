<?php
namespace php\swing;

class Timer {
    /**
     * @var bool
     */
    public $repeat;

    /**
     * Delay in milliseconds for repeats
     * @var int
     */
    public $delay;

    /**
     * Initial Delay in milliseconds for first trigger
     * @var int
     */
    public $initDelay;


    /**
     * User data
     * @var string
     */
    public $actionCommand;

    /**
     * @param int $delay
     * @param callable $callback
     */
    public function __construct($delay, callable $callback) { }

    /**
     * Start Timer
     */
    public function start() { }

    /**
     * Stop Timer
     */
    public function stop() { }

    /**
     * Restart Timer
     */
    public function restart() { }

    /**
     * @return bool
     */
    public function isRunning() { }
}
