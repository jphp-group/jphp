<?php
namespace php\gui;


/**
 * Class UXScreen
 * @package php\gui
 * @packages gui, javafx
 */
abstract class UXScreen
{


    /**
     * @readonly
     * @var double
     */
    public $dpi;

    /**
     * @readonly
     * @var array
     */
    public $bounds = ['x' => 0, 'y' => 0, 'width' => 0, 'height' => 0];

    /**
     * @readonly
     * @var array
     */
    public $visualBounds = ['x' => 0, 'y' => 0, 'width' => 0, 'height' => 0];

    /**
     * @return UXScreen
     */
    public static function getPrimary()
    {
    }

    /**
     * @return UXScreen[]
     */
    public static function getScreens()
    {
    }
}