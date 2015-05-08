<?php
namespace ext\javafx;

/**
 * Class UXScene
 * @package ext\javafx
 *
 * @property double $width
 * @property double $height
 */
class UXScene
{
    /**
     * @var UXParent
     */
    public $root;

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