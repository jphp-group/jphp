<?php
namespace ext\javafx;
use ext\javafx\text\UXFont;

/**
 * Class UXLabeled
 * @package ext\javafx
 */
abstract class UXLabeled extends UXControl
{
    /**
     * @var string
     */
    public $text;

    /**
     * @var UXFont
     */
    public $font;
}