<?php
namespace php\gui;

use php\io\File;

/**
 * Class UXFileChooser
 * @package php\gui
 *
 * @packages gui, javafx
 */
class UXFileChooser
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
     * @var string
     */
    public $initialFileName = null;

    /**
     * @var int
     */
    public $selectedExtensionFilter = -1;

    /**
     * @var array [description, extensions: [...]]
     */
    public $extensionFilters = [];

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
    public function showOpenDialog(UXWindow $window = null)
    {
    }

    /**
     * @param UXWindow|null $window
     * @return File
     */
    public function showSaveDialog(UXWindow $window = null)
    {
    }

    /**
     * @param UXWindow|null $window
     * @return File[]
     */
    public function showOpenMultipleDialog(UXWindow $window = null)
    {
    }
}