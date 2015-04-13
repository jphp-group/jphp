<?php
namespace php\gdx;

use php\gdx\files\FileHandle;

/**
 * Class Files
 * @package php\gdx
 */
class Files {
    /**
     * @param string $path
     * @param string $type - Classpath, Internal, External, Absolute, Local
     * @return FileHandle
     */
    public function getFileHandle($path, $type) { }

    /**
     * @param string $path
     * @return FileHandle
     */
    public function classpath($path) { }

    /**
     * @param $path
     * @return FileHandle
     */
    public function internal($path) { }

    /**
     * @param $path
     * @return FileHandle
     */
    public function external($path) { }

    /**
     * @param $path
     * @return FileHandle
     */
    public function absolute($path) { }

    /**
     * @param $path
     * @return FileHandle
     */
    public function local($path) { }

    /**
     * @return string
     */
    public function getExternalStoragePath() {}

    /**
     * @return bool
     */
    public function isExternalStorageAvailable() { }

    /**
     * @return string
     */
    public function getLocalStoragePath() { }

    /**
     * @return string
     */
    public function isLocalStorageAvailable() { }
} 