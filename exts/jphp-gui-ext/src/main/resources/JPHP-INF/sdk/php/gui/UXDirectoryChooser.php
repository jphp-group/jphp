<?php
namespace php\gui;

use php\io\File;

/**
 * Class UXDirectoryChooser
 * @package php\gui
 * @packages gui, javafx
 */
class UXDirectoryChooser
{


    /**
     * @var string
     */
    public $title;

    /**
     * @var File|string
     */
    public $initialDirectory;

    /**
     * @return File
     */
    public function execute()
    {
    }

    /**
     * @param UXWindow|null $window
     * @return File
     */
    public function showDialog(UXWindow $window = null)
    {
    }
}