<?php
namespace ext\javafx;

use ext\xml\DomDocument;

/**
 * Class UXWebEngine
 * @package ext\javafx
 */
abstract class UXWebEngine
{
    /**
     * @readonly
     * @var DomDocument
     */
    public $document;

    /**
     * @var bool
     */
    public $javaScriptEnabled;

    /**
     * @var string
     */
    public $userStyleSheetLocation;

    /**
     * @readonly
     * @var string
     */
    public $location;

    /**
     * @readonly
     * @var string
     */
    public $title;

    /**
     * @param string $url
     */
    public function load($url) {}

    /**
     * @param string $content
     * @param string $contentType (optional)
     */
    public function loadContent($content, $contentType) {}

    /**
     * ...
     */
    public function reload() {}

    /**
     * @param string $script
     * @return mixed
     */
    public function executeScript($script) {}
}