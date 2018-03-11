<?php
namespace php\gui;

/**
 * Class UXWebView
 * @package php\gui
 * @packages gui, javafx
 */
class UXWebView extends UXParent
{
    /**
     * @var double[]
     */
    public $minSize = [-1, -1];

    /**
     * @var double[]
     */
    public $maxSize = [-1, -1];

    /**
     * @var double
     */
    public $minWidth, $minHeight = -1;

    /**
     * @var double
     */
    public $maxWidth, $maxHeight = -1;

    /**
     * @var double
     */
    public $zoom = 1.0;

    /**
     * @readonly
     * @var UXWebEngine
     */
    public $engine;

    /**
     * @var bool
     */
    public $contextMenuEnabled;

    /**
     * See also ->engine->url (this is alias)
     * @var string
     */
    public $url;
}