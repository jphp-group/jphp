<?php
namespace ext\javafx;

use php\io\Stream;

/**
 * Class UXImage
 * @package ext\javafx
 */
class UXImage {
    /**
     * @readonly
     * @var double
     */
    public $width;

    /**
     * @readonly
     * @var double
     */
    public $height;

    /**
     * @readonly
     * @var double
     */
    public $progress;

    /**
     * @param Stream $stream
     */
    public function __construct(Stream $stream) {}

    /**
     * ...
     */
    public function cancel() {}

    /**
     * @param string $url
     * @param bool $background
     * @return UXImage
     */
    public static function ofUrl($url, $background = false) {}
}