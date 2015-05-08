<?php
namespace ext\javafx;

/**
 * Class UXLabel
 * @package ext\javafx
 */
class UXLabel extends UXLabeled
{
    /**
     * @var UXNode
     */
    public $labelFor;

    /**
     * @param string $text (optional)
     * @param UXNode $graphic (optional)
     */
    public function __construct($text, UXNode $graphic) {}
}