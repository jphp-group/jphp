<?php
namespace php\gui;


/**
 * Class UXMediaPlayer
 * @package php\gui
 * @packages gui, javafx
 */
class UXMediaPlayer
{
    /**
     * @var double form -1.0 to 1.0
     */
    public $balance = 0;

    /**
     * @var double from 0.0 to 8.0
     */
    public $rate = 1.0;

    /**
     * @var double
     */
    public $volume = 1.0;

    /**
     * @var bool
     */
    public $mute = false;

    /**
     * @var int -1 is
     */
    public $cycleCount = 1;

    /**
     * UNKNOWN, READY, PAUSED, PLAYING, STOPPED, STALLED, HALTED, DISPOSED
     * @readonly
     * @var string
     */
    public $status = 'UNKNOWN';

    /**
     * @readonly
     * @var double
     */
    public $currentRate;

    /**
     * @var int in millis
     */
    public $currentTime;

    /**
     * @var int in percent from 0 to 100
     */
    public $currentTimeAsPercent;

    /**
     * @readonly
     * @var int
     */
    public $currentCount;

    /**
     * @var UXMedia
     */
    public $media;

    /**
     * @param UXMedia $media
     */
    public function __construct(UXMedia $media)
    {
    }

    public function play()
    {
    }

    public function pause()
    {
    }

    public function stop()
    {
    }

    /**
     * @param $time in millis
     */
    public function seek($time)
    {
    }
}