<?php

namespace php\gui;

/**
 * Class UXParent
 * @package php\gui
 * @packages gui, javafx
 */
abstract class UXParent extends UXNode
{


    /**
     * @var UXList
     */
    public $childrenUnmodifiable;

    /**
     * Список css таблиц стилей (пути к файлам и ресурсам).
     * @readonly
     * @var UXList of string (paths for css style sheets)
     */
    public $stylesheets;

    /**
     * Executes a top-down layout pass on the scene graph under this parent.
     */
    public function layout() {}

    /**
     * Requests a layout pass to be performed before the next scene is rendered.
     */
    public function requestLayout() {}

    /**
     * @param $filter (UXNode $node) : boolean
     */
    public function findNode(callable $filter)
    {
    }

    /**
     * @return UXNode|null
     */
    public function findFocusedNode()
    {
    }
}