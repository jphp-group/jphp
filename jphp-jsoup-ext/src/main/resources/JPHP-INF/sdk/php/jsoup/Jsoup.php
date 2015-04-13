<?php
namespace php\jsoup;

/**
 * Class Jsoup
 * @package php\jsoup
 */
final class Jsoup {
    private function __construct() { }

    /**
     * @param string $url
     * @return Connection
     */
    public static function connect($url) { return new Connection(); }
}