<?php
namespace php\gui\layout;

/**
 * Class UXFlowPane
 * @package php\gui\layout
 * @packages gui, javafx
 */
class UXFlowPane extends UXPane
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
    public $prefWrapLength = 400;

    /**
     * @var string
     */
    public $rowValignment = 'CENTER';

    /**
     * @var string
     */
    public $columnHalignment = 'LEFT';

    /**
     * @var string
     */
    public $alignment = 'CENTER_LEFT';
}