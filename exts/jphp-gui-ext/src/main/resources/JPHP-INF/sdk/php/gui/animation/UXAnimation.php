<?php
namespace php\gui\animation;

/**
 * Class UXAnimation
 * @package php\gui\animation
 *
 * @packages gui, javafx
 */
abstract class UXAnimation
{
    /**
     * @readonly
     * @var string PAUSED, RUNNING, STOPPED
     */
    public $status;

    /**
     * @var double
     */
    public $rate;

    /**
     * @readonly
     * @var double
     */
    public $currentRate;

    /**
     * @var int
     */
    public $cycleCount;

    /**
     * @readonly
     * @var double
     */
    public $targetFramerate;

    /**
     * @var bool
     */
    public $autoReverse;

    /**
     * @readonly
     * @var int millis
     */
    public $currentTime;

    /**
     * @readonly
     * @var int millis
     */
    public $cycleDuration;

    /**
     * @readonly
     * @var int millis
     */
    public $totalDuration;

    /**
     * @var int millis
     */
    public $delay;


    public function play()
    {
    }

    public function playFromStart()
    {
    }

    /**
     * @param int $millis
     */
    public function playFrom($millis)
    {
    }

    /**
     * @param int $millis
     */
    public function jumpTo($millis)
    {
    }

    public function stop()
    {
    }

    public function pause()
    {
    }

    /**
     * @param string $event finish, etc.
     * @param callable $handler
     * @param string $group
     */
    public function on($event, callable $handler, $group = 'general')
    {
    }

    /**
     * @param string $event
     * @param string $group (optional)
     */
    public function off($event, $group)
    {
    }

    /**
     * @param string $event
     * @param UXEvent $e (optional)
     */
    public function trigger($event, UXEvent $e)
    {
    }
}