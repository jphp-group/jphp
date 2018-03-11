<?php
namespace php\gui\layout;

/**
 * @package php\gui\layout
 * @packages gui, javafx
 */
class UXTilePane extends UXPane
{
    /**
     * @var string HORIZONTAL or VERTICAL
     */
    public $orientation = 'HORIZONTAL';

    /**
     * @var float
     */
    public $hgap = 0.0;

    /**
     * @var float
     */
    public $vgap = 0.0;

    /**
     * @var float
     */
    public $prefTileWidth;

    /**
     * @var float
     */
    public $prefTileHeight;

    /**
     * @var int
     */
    public $prefColumns;

    /**
     * @var int
     */
    public $prefRows;

    /**
     * @var string
     */
    public $alignment = 'CENTER_LEFT';

    /**
     * @var string
     */
    public $tileAlignment = 'CENTER_LEFT';
}