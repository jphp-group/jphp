<?php
namespace php\gui;

/**
 * Class UXScene
 * @package php\gui
 * @packages gui, javafx
 */
class UXScene
{


    /**
     * @var UXParent
     */
    public $root;

    /**
     * @readonly
     * @var UXWindow
     */
    public $window;

    /**
     * @var string
     */
    public $cursor;

    /**
     * @param UXParent $parent
     * @param double[] $size (optional)
     */
    public function __construct(UXParent $parent, array $size) {}

    /**
     * @param string $path filename, url or Stream
     */
    public function addStylesheet($path) {}

    /**
     * ...
     */
    public function clearStylesheets() {}

    /**
     * @return string[]
     */
    public function getStylesheets() {}
}