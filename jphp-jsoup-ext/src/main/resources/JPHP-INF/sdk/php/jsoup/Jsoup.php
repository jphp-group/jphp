<?php
namespace php\jsoup;
use php\io\File;
use php\io\Stream;

/**
 * Class Jsoup
 * @package php\jsoup
 */
final class Jsoup
{
    const __PACKAGE__ = 'jsoup';

    private function __construct()
    {
    }

    /**
     * @param string $url
     * @return Connection
     */
    public static function connect($url)
    {
    }

    /**
     * @param string|File|Stream $source
     * @param string $encoding
     * @param string $baseUri
     * @return Document
     */
    public static function parse($source, $encoding, $baseUri)
    {
    }

    /**
     * @param string $text
     * @param string $baseUri [optional]
     * @return Document
     */
    public static function parseText($text, $baseUri)
    {
    }
}