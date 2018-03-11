<?php
namespace php\gui\printing;

use php\gui\UXNode;
use php\gui\UXWindow;

class UXPrinterJob
{
    /**
     * UXPrinterJob constructor.
     * @param UXPrinter $printer
     */
    public function __construct(UXPrinter $printer)
    {
    }

    /**
     * @return UXPrinter
     */
    public function getPrinter()
    {
    }

    /**
     * NOT_STARTED, PRINTING, CANCELED, ERROR, DONE.
     * @return string
     */
    public function getJobStatus()
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
     * Cancel print job.
     */
    public function cancel()
    {
    }

    /**
     * Finish print job.
     * @return bool
     */
    public function end()
    {
    }

    /**
     * @param UXWindow $ownerWindow
     * @return bool
     */
    public function showPrintDialog(UXWindow $ownerWindow)
    {
    }

    /**
     * @param UXWindow $ownerWindow
     * @return bool
     */
    public function showPageSetupDialog(UXWindow $ownerWindow)
    {
    }

    /**
     * Settings is an array like:
     *
     *      jobName => string,
     *      copies => int,
     *      collation => string (UNCOLLATED, COLLATED)
     *      printSides => string (ONE_SIDED, DUPLEX, TUMBLE)
     *      printColor => string (COLOR, MONOCHROME)
     *      printQuality => string (DRAFT, LOW, NORMAL, HIGH)
     *
     * @param array $settings
     */
    public function setSettings(array $settings)
    {
    }
}