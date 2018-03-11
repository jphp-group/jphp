<?php
namespace php\gui;

use php\gui\layout\UXPanel;

/**
 * Class UXMediaViewBox
 * @package php\gui
 * @packages gui, javafx
 */
class UXMediaViewBox extends UXPanel
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