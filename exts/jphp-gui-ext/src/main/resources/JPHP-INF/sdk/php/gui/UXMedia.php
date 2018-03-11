<?php
namespace php\gui;

/**
 * Class UXMedia
 * @package php\gui
 * @packages gui, javafx
 */
class UXMedia
{


    /**
     * @readonly
     * @var int in millis
     */
    public $duration;

    /**
     * @readonly
     * @var int
     */
    public $width;

    /**
     * @readonly
     * @var int
     */
    public $height;

    /**
     * @readonly
     * @var string
     */
    public $source;

    /**
     * @param string $source
     */
    public function __construct($source)
    {
    }

    /**
     * @param string $path
     * @return UXMedia
     */
    public static function createFromResource($path)
    {
    }

    /**
     * @param $url
     * @return string
     */
    public static function createFromUrl($url)
    {
    }
}