<?php
namespace php\gui\animation;

/**
 * Class UXKeyFrame
 * @package php\gui\animation
 *
 * @packages gui, javafx
 */
class UXKeyFrame
{
    /**
     * @var int in millis
     */
    public $time;

    /**
     * @var string
     */
    public $name;

    /**
     * @param int $time in millis
     * @param callable $onFinished
     * @param null|string $name
     */
    public function __construct($time, callable $onFinished, $name = null)
    {
    }
}