<?php
namespace ext\javafx;


/**
 * Class UXStage
 * @package ext\javafx
 *
 * @property double $maxHeight
 * @property double $maxWidth
 * @property double $minHeight
 * @property double $minWidth
 *
 * @property bool $fullScreen
 * @property bool $iconified
 * @property bool $resizable
 */
class UXStage extends UXWindow
{
    /**
     * @var string
     */
    public $title;

    /**
     * NONE, WINDOW_MODAL, APPLICATION_MODAL
     * @var string
     */
    public $modality;

    /**
     * @var UXWindow
     */
    public $owner;

    /**
     * @readonly
     * @var string
     */
    public $style;

    /**
     * @param string $style (optional) - DECORATED, UNDECORATED, TRANSPARENT, UTILITY
     */
    public function __construct($style) {}

    /**
     * ...
     */
    public function showAndWait() {}

    /**
     * ...
     */
    public function toBack() {}

    /**
     * ...
     */
    public function toFront() {}
}