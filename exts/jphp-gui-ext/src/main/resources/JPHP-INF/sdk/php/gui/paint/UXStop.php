<?php


namespace php\gui\paint;


/**
 * Class UXStop
 * @package php\gui\paint
 * @packages gui, javafx
 */
final class UXStop{
    /**
     * @readonly
     * @var UXColor
     */
    public $color;
    /**
     * @readonly
     * @var double
     */
    public $offset;

    /**
     * UXStop constructor.
     * @param float $offset
     * @param UXColor|string $color
     */
    public function __construct($offset, $color){

    }
}