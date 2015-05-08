<?php
namespace ext\javafx;
use ext\javafx\event\UXEvent;
use ext\javafx\layout\UXPane;

/**
 * Class UXWindow
 * @package ext\javafx
 *
 * @property double $x
 * @property double $y
 * @property double $width
 * @property double $height
 * @property double $opacity
 * @property bool $focused
 * @property bool $showing
 */
abstract class UXWindow
{
    /**
     * @var UXScene
     */
    public $scene;

    /**
     * [width, height]
     * @var double[]
     */
    public $size;

    /**
     * @var UXPane
     */
    public $layout;

    /**
     * ...
     */
    public function show() {}

    /**
     * ...
     */
    public function hide() {}

    /**
     * ...
     */
    public function centerOnScreen() {}

    /**
     * ...
     */
    public function sizeToScene() {}

    /**
     * @param string $event
     * @param callable $handler
     * @param string $group
     */
    public function on($event, callable $handler, $group = 'general') { }

    /**
     * @param string $event
     * @param string $group (optional)
     */
    public function off($event, $group) {}

    /**
     * @param string $event
     * @param UXEvent $e (optional)
     */
    public function trigger($event, UXEvent $e) {}

    /**
     * @param string $id
     * @return UXNode|null
     */
    public function __get($id) {}

    /**
     * @param string $id
     */
    public function __isset($id) {}
}