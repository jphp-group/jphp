<?php
namespace php\swing;

use php\io\File;
use php\io\IOException;
use php\io\Stream;
use php\lang\JavaObject;

class Image {
    const TYPE_INT_RGB        = 1;
    const TYPE_INT_ARGB       = 2;
    const TYPE_INT_ARGB_PRE   = 3;
    const TYPE_INT_BGR        = 4;
    const TYPE_3BYTE_BGR      = 5;
    const TYPE_4BYTE_ABGR     = 6;
    const TYPE_4BYTE_ABGR_PRE = 7;
    const TYPE_USHORT_565_RGB = 8;
    const TYPE_USHORT_555_RGB = 9;
    const TYPE_BYTE_GRAY      = 10;
    const TYPE_USHORT_GRAY    = 11;
    const TYPE_BYTE_BINARY    = 12;
    const TYPE_BYTE_INDEXED   = 13;

    /**
     * @readonly
     * @var int
     */
    public $type;

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
     * @param int $width
     * @param int $height
     * @param int $type
     */
    public function __construct($width, $height, $type = self::TYPE_INT_RGB) { }

    /**
     * @param int $x
     * @param int $y
     * @param int $w
     * @param int $h
     * @return Image
     */
    public function getSubimage($x, $y, $w, $h) { }

    /**
     * @param int $x
     * @param int $y
     * @return int
     */
    public function getRGB($x, $y){ }

    /**
     * @param int $x
     * @param int $y
     * @param int $rgb - color
     */
    public function setRGB($x, $y, $rgb) { }

    /**
     * @return Graphics
     */
    public function getGraphics(){ }

    /**
     * @param $name
     * @return JavaObject|null
     */
    public function getProperty($name) { }

    /**
     * @param Stream|File|string $stream - file path or stream
     * @return Image
     * @throws IOException
     */
    public static function read($stream) { }

    /**
     * @param Image $image
     * @param $format
     * @param Stream|File|string $stream - file path or stream
     * @throws IOException
     */
    public static function write(Image $image, $format, $stream) { }
}
