<?php
namespace php\gui;

use php\graphic\Image;
use php\gui\paint\UXColor;
use php\io\File;
use php\io\Stream;

/**
 * Image Class.
 * --ru--
 * Класс изображения.
 *
 * @package php\gui
 * @packages gui, javafx
 */
class UXImage
{
    /**
     * Width of image.
     * --ru--
     * Ширина картинки.
     * @readonly
     * @var double
     */
    public $width;

    /**
     * Height of image.
     * --ru--
     * Высота картинки.
     * @readonly
     * @var double
     */
    public $height;

    /**
     * Progress of loading.
     * --ru--
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
     * Returns color of pixel.
     * --ru--
     * Возвращает цвет пикселя картинки.
     * @param int $x
     * @param int $y
     * @return UXColor
     */
    public function getPixelColor($x, $y)
    {
    }

    /**
     * Returns color of pixel as ARGB int value.
     * --ru--
     * Возвращает цвет пикселя картинки в формате argb.
     * @param int $x
     * @param int $y
     * @return int
     */
    public function getPixelARGB($x, $y)
    {
    }

    /**
     * Cancel loading of image.
     * --ru--
     * Отменяет загрузку картинки.
     */
    public function cancel() {}

    /**
     * Occurs an error when loading?
     * --ru--
     * Произошла ли ошибка при загрузке?
     *
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
     * --ru--
     * Сконвертировать в нативное изображение.
     *
     * @return null|Image
     */
    public function toNative(): ?Image
    {
    }

    /**
     * Create from native image.
     * --ru--
     * Создать изображение из нативного.
     *
     * @param Image $image
     * @return UXImage
     */
    public static function ofNative(Image $image): ?UXImage
    {
    }

    /**
     * Create image from URL.
     * --ru--
     * Создает новую картинку из URL.
     *
     * @param string $url
     * @param bool $background
     * @return UXImage
     */
    public static function ofUrl($url, $background = false) {}
}