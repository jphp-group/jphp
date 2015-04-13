<?php
namespace php\gdx\graphics;

use php\gdx\files\FileHandle;

/**
 * Class Texture
 * @package php\gdx\graphics
 */
class Texture {
    /**
     * @param FileHandle $fileHandle
     * @param bool $useMipMaps (optional)
     * @param string $format (optional)
     * @return Texture
     */
    public static function ofFile(FileHandle $fileHandle, $useMipMaps = false, $format) { }

    /**
     * @param Pixmap $pixmap
     * @param bool $useMipMaps (optional)
     * @param string $format (optional)
     */
    public function __construct(Pixmap $pixmap, $useMipMaps = false, $format) { }


    /**
     * @return int
     */
    public function getWidth() { }

    /**
     * @return int
     */
    public function getHeight() { }

    /**
     * @return int
     */
    public function getDepth() { }

    /**
     * @return bool
     */
    public function isManaged() { }
} 