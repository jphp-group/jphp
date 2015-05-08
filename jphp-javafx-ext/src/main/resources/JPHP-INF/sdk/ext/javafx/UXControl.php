<?php
namespace ext\javafx;

/**
 * Class UXControl
 * @package ext\javafx
 */
abstract class UXControl extends UXParent
{
    /**
     * @var UXTooltip
     */
    public $tooltip = null;

    /**
     * @var UXContextMenu
     */
    public $contextMenu = null;
}