<?php
namespace php\gui;

use php\graphic\Image;
use php\gui\paint\UXColor;
use php\io\File;
use php\io\Stream;

/**
 * Class UXImage
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXImage
{
    /**
     * Ширина картинки.
     * @readonly
     * @var double
     */
    public $width;

    /**
     * Высота картинки.
     * @readonly
     * @var double
     */
    public $height;

    /**
     * Прогресс загрузки.
     * @readonly
     * @var double
     */
    public $progress;

    /**
     * @param Stream|string|Image $stream
     * @param $requiredWidth (optional)
     * @param $requiredHeight (optional)
     * @param bool $proportional
     */
    public function __construct($stream, $requiredWidth = false, $requiredHeight = false, $proportional = true) {}

    /**
     * Возвращает цвет пикселя картинки.
     * @param int $x
     * @param int $y
     * @return UXColor
     */
    public function getPixelColor($x, $y)
    {
    }

    /**
     * Возвращает цвет пикселя картинки в формате argb.
     * @param int $x
     * @param int $y
     * @return int
     */
    public function getPixelARGB($x, $y)
    {
    }

    /**
     * Отменяет загрузку картинки.
     */
    public function cancel() {}

    /**
     * @return bool
     */
    public function isError()
    {
    }

    /**
     * @return bool
     */
    public function isBackgroundLoading()
    {
    }

    /**
     * Save image to file or stream in passed format, by default png.
     * --RU--
     * Сохранить изображение в файл или поток в переданном формате, по-умолчанию png.
     *
     * @param string|Stream|File $to
     * @param string $format
     */
    public function save($to, $format = 'png')
    {
    }

    /**
     * Convert to native image.
     * @return null|Image
     */
    public function toNative(): ?Image
    {
    }

    /**
     * Create from native image.
     *
     * @param Image $image
     * @return UXImage
     */
    public static function ofNative(Image $image): ?UXImage
    {
    }

    /**
     * Создает новую картинку из URL.
     *
     * @param string $url
     * @param bool $background
     * @return UXImage
     */
    public static function ofUrl($url, $background = false) {}
}