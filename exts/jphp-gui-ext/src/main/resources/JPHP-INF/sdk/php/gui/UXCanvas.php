<?php
namespace php\gui;

use php\gui\paint\UXColor;
use php\io\File;
use php\io\Stream;

/**
 * Class UXCanvas
 * @package php\gui
 * @packages gui, javafx
 */
class UXCanvas extends UXNode
{
    /**
     * @return UXGraphicsContext
     */
    public function getGraphicsContext()
    {
    }

    /**
     * Alias of getGraphicsContext().
     *
     * @return UXGraphicsContext
     */
    public function gc()
    {
    }

    /**
     * Save image of canvas to file or stream in passed format, by default png.
     * --RU--
     * Сохранить изображение полотна в файл или поток в переданном формате, по-умолчанию png.
     *
     * @param string|Stream|File $to
     * @param string $format
     */
    public function save($to, $format = 'png')
    {
    }

    /**
     * @deprecated
     * @param string $format png, gif, etc.
     * @param Stream|File|string $output
     * @param callable $callback (bool $success)
     * @param null|UXColor $transparentColor
     */
    public function writeImageAsync($format, $output, UXColor $transparentColor, callable $callback)
    {
    }
}