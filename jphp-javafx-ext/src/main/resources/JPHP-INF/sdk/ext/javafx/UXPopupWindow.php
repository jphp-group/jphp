<?php
namespace ext\javafx;

/**
 * Class UXPopupWindow
 * @package ext\javafx
 */
class UXPopupWindow extends UXWindow
{
    /**
     * @var bool
     */
    public $autoFix;

    /**
     * @var bool
     */
    public $autoHide;

    /**
     * @var bool
     */
    public $hideOnEscape;

    /**
     * @param UXWindow $window
     * @param double $screenX (optional)
     * @param double $screenY (optional)
     */
    public function show(UXWindow $window, $screenX, $screenY) {}

    /**
     * ...
     */
    public function hide() {}
}