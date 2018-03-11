<?php
namespace php\gui\animation;

/**
 * Class UXAnimationTimer
 * @package php\gui\animation
 *
 * @packages gui, javafx
 */
class UXAnimationTimer
{
    const FRAME_INTERVAL = 1 / 60;
    const FRAME_INTERVAL_MS = self::FRAME_INTERVAL * 1000;

    /**
     * @param callable $handler ($now)
     */
    public function __construct(callable $handler)
    {
    }

    public function start()
    {
    }

    public function stop()
    {
    }
}