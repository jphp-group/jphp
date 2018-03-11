<?php
namespace php\gui;

use php\io\File;

/**
 * Class UXClipboard
 * --RU--
 * Класс для работы с буфером обмена.
 *
 * @package php\gui
 * @packages gui, javafx
 */
class UXClipboard
{


    private function __construct()
    {
    }

    /**
     * Clear clipboard.
     * --RU--
     * Очистить буфер.
     */
    public static function clear()
    {
    }

    /**
     * --RU--
     * Возвращает true если буфер содержит какой-то текст.
     *
     * @return bool
     */
    public static function hasText()
    {
    }

    /**
     * @return bool
     */
    public static function hasUrl()
    {
    }

    /**
     * @return bool
     */
    public static function hasHtml()
    {
    }

    /**
     * @return bool
     */
    public static function hasImage()
    {
    }

    /**
     * @return bool
     */
    public static function hasFiles()
    {
    }

    /**
     * @return string
     */
    public static function getHtml()
    {
    }

    /**
     * @return string
     */
    public static function getUrl()
    {
    }

    /**
     * @return string
     */
    public static function getText()
    {
    }

    /**
     * @return UXImage
     */
    public static function getImage()
    {
    }

    /**
     * @return File[]
     */
    public static function getFiles()
    {
    }

    /**
     * --RU--
     * Помещает в буфер указанный текст.
     *
     * @param string $text
     */
    public static function setText($text)
    {
    }

    /**
     * @param array $content [text => string, html => string, url => string, image => UXImage, files => [...]]
     */
    public static function setContent(array $content)
    {
    }
}