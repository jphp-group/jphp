<?php
namespace php\swing;

use php\io\File;

/**
 * Class UIFileChooser
 * @package php\swing
 */
class UIFileChooser extends UIContainer {
    const FILES_ONLY            = 0;
    const DIRECTORIES_ONLY      = 1;
    const FILES_AND_DIRECTORIES = 2;

    /**
     * @var string
     */
    public $dialogTitle;

    /**
     * @var string
     */
    public $approveButtonText;

    /**
     * @var File
     */
    public $selectedFile;

    /**
     * @var File[]
     */
    public $selectedFiles;

    /**
     * @var bool
     */
    public $multiSelection;

    /**
     * Example: FILES_AND_DIRECTORIES, FILES_ONLY or DIRECTORIES_ONLY
     * @var int
     */
    public $selectionMode;

    /**
     * @var bool
     */
    public $dragEnabled;

    /**
     * @var bool
     */
    public $fileHiding;

    /**
     * @var bool
     */
    public $controlButtonVisible;

    /**
     * @var bool
     */
    public $acceptAllFileFilterUsed;

    /**
     * @param string $approveButtonText
     * @param UIWindow $parent
     * @return bool
     */
    public function showDialog($approveButtonText, UIWindow $parent = null) { }

    /**
     * @param UIWindow $parent
     * @return bool
     */
    public function showSaveDialog(UIWindow $parent = null) { }

    /**
     * @param UIWindow $parent
     * @return bool
     */
    public function showOpenDialog(UIWindow $parent = null) { }

    /**
     * @param callable $filter  (File $file) -> bool
     * @param string $description
     */
    public function addChoosableFilter(callable $filter, $description) { }

    /**
     * @param array $extensions   ['jpg', 'gif', 'png', ... etc]
     * @param string $description
     * @param bool $showDirectories
     */
    public function addChoosableExtensions(array $extensions, $description, $showDirectories = true) { }

    /**
     * removes all added file filters
     */
    public function resetChoosableFilters() { }

    /**
     * @param string|File $file
     * @return bool
     */
    public function isTraversable($file) { }

    /**
     * @param string|File $file
     */
    public function ensureFileIsVisible($file) { }

    public function approveSelection() { }

    public function cancelSelection() { }

    public function changeToParentDirectory() { }
}
