<?php
namespace php\gui;

/**
 * Class UXMediaView
 * @package php\gui
 * @packages gui, javafx
 */
class UXMediaView extends UXNode
{


    /**
     * @var UXMediaPlayer
     */
    public $player = null;

    /**
     * @var bool
     */
    public $smooth = false;

    /**
     * @var bool
     */
    public $proportional = false;

    /**
     * @param string $filename
     * @param bool $autoPlay
     */
    public function open($filename, $autoPlay = true)
    {
    }

    /**
     * Play media.
     */
    public function play()
    {
    }

    /**
     * Stop playing.
     */
    public function stop()
    {
    }

    /**
     * Pause playing.
     */
    public function pause()
    {
    }
}