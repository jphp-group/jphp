<?php

namespace ext\javafx;

/**
 * Class UXParent
 * @package ext\javafx
 */
abstract class UXParent extends UXNode
{
    /**
     * Executes a top-down layout pass on the scene graph under this parent.
     */
    public function layout() {}

    /**
     * Requests a layout pass to be performed before the next scene is rendered.
     */
    public function requestLayout() {}

    /**
     * @return UXNode[]
     */
    public function getChildren() {}
}