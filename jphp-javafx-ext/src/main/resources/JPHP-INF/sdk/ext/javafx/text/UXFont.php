<?php
namespace ext\javafx\text;
use php\io\Stream;

/**
 * Class UXFont
 * @package ext\javafx\text
 */
class UXFont
{
    /**
     * @param double $size
     * @param string $family (optional)
     */
    public function __construct($size, $family) { }

    /**
     * @param string $family
     * @param double $size
     * @return UXFont
     */
    public static function of($family, $size) {}

    /**
     * @param Stream $stream
     * @param double $size
     * @return UXFont
     */
    public static function load(Stream $stream, $size) {}

    /**
     * @return UXFont
     */
    public static function getDefault() { }
}