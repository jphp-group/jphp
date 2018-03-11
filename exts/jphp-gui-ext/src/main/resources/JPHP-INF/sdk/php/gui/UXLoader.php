<?php
namespace php\gui;
use php\io\File;
use php\io\Stream;

/**
 * Class UXLoader
 * @package php\gui
 * @packages gui, javafx
 */
class UXLoader
{


    /**
     * @var string
     */
    public $location;

    /**
     * @param string $location [optional]
     */
    public function __construct($location) {}

    /**
     * @param string|File|Stream $source [optional]
     * @return UXNode
     */
    public function load($source) {}

    /**
     * @param $string
     * @return UXNode
     */
    public function loadFromString($string)
    {
    }
}