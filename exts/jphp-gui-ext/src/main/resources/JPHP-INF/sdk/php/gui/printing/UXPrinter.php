<?php
namespace php\gui\printing;

use php\gui\UXNode;
use php\gui\UXWindow;

/**
 * @packages gui, javafx
 */
class UXPrinter
{
    /**
     * @readonly
     * @var string
     */
    public $name;

    /**
     * @readonly
     * @var array
     */
    public $attributes = [];

    private function __construct()
    {
    }

    /**
     * @return UXPrinterJob
     */
    public function createPrintJob()
    {
    }

    /**
     * @param UXNode $node
     * @return bool
     */
    public function print(UXNode $node)
    {
    }

    /**
     * @param UXWindow $ownerWindow
     * @param UXNode $node
     * @return bool
     */
    public function printWithDialog(UXWindow $ownerWindow, UXNode $node)
    {
    }

    /**
     * @return UXPrinter
     */
    public static function getDefault()
    {
    }

    /**
     * @return UXPrinter[]
     */
    public static function getAll()
    {
    }
}