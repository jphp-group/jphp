<?php
namespace ext\javafx;
use php\io\File;
use php\io\Stream;

/**
 * Class UXLoader
 * @package ext\javafx
 */
class UXLoader
{
    /**
     * @var string
     */
    public $location;

    /**
     * @param string $location (optional)
     */
    public function __construct($location) {}

    /**
     * @param string|File|Stream $source (optional)
     * @return UXNode
     */
    public function load($source) {}

    /**
     * @param string|File|Stream $source (optional)
     * @return UXStage
     */
    public function loadAsStage($source) {}
}