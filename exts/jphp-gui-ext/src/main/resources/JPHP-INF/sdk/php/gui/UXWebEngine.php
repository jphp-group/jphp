<?php
namespace php\gui;

use php\gui\printing\UXPrinterJob;
use php\xml\DomDocument;

/**
 * Class UXWebEngine
 * @package php\gui
 * @packages gui, javafx
 */
abstract class UXWebEngine
{
    /**
     * @readonly
     * @var DomDocument
     */
    public $document;

    /**
     * @readonly
     * @var UXWebHistory
     */
    public $history;

    /**
     * @var bool
     */
    public $javaScriptEnabled;

    /**
     * @var string
     */
    public $userStyleSheetLocation;

    /**
     * @var string
     */
    public $userDataDirectory;

    /**
     * @var string
     */
    public $url;

    /**
     * @var string
     */
    public $userAgent;

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
     * @readonly
     * @var string READY, SCHEDULED, RUNNING, SUCCEEDED, CANCELLED, FAILED
     */
    public $state;

    /**
     * @param string $url
     */
    public function load($url) {}

    /**
     * @param string $content
     * @param string $contentType (optional)
     */
    public function loadContent($content, $contentType = null) {}

    /**
     * ...
     */
    public function reload() {}

    /**
     * See reload().
     */
    public function refresh() {}

    /**
     * Break a loading.
     */
    public function cancel() {}

    /**
     * @param string $script
     * @return mixed
     */
    public function executeScript($script) {}

    /**
     * @param string $name
     * @param array $args
     * @return mixed
     */
    public function callFunction($name, array $args)
    {
    }

    /**
     * @param string $name
     * @param callable $handler
     */
    public function addSimpleBridge($name, callable $handler)
    {
    }

    /**
     * @param callable $handler (UXWebEngine $self, $old, $new)
     */
    public function watchState(callable $handler)
    {
    }

    /**
     * @param UXPrinterJob $printerJob
     */
    public function print(UXPrinterJob $printerJob)
    {
    }

    /**
     * @param string $event
     * @param callable $handler
     * @param string $group
     */
    public function on($event, callable $handler, $group = 'general')
    {
    }

    /**
     * @param string $event
     * @param string $group (optional)
     */
    public function off($event, $group)
    {
    }

    /**
     * @param string $event
     * @param UXEvent $e (optional)
     */
    public function trigger($event, UXEvent $e)
    {
    }
}