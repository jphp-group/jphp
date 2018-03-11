<?php
namespace php\gui;
use php\gui\layout\UXRegion;

/**
 * Class UXControl
 * @package php\gui
 * @packages gui, javafx
 */
abstract class UXControl extends UXRegion
{


    /**
     * Подсказка (тултип).
     * @var UXTooltip
     */
    public $tooltip = null;

    /**
     * Текстовая подсказка.
     * @var string
     */
    public $tooltipText = null;

    /**
     * Контекстное меню.
     * @var UXContextMenu
     */
    public $contextMenu = null;
}