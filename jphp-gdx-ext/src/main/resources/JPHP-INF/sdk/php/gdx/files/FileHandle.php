<?php
namespace php\gdx\files;

use php\io\File;
use php\io\Stream;

/**
 * Class FileHandle
 * @package php\gdx\files
 */
class FileHandle {

    /**
     * @param string|File $path
     */
    public function __construct($path) { }

    /**
     * @return string
     */
    public function path() { }

    /**
     * @return string
     */
    public function name() { }

    /**
     * @return string
     */
    public function extension() { }

    /**
     * @return string
     */
    public function nameWithoutExtension() { }

    /**
     * @return string
     */
    public function pathWithoutExtension() { }

    /**
     * @return string
     */
    public function type() { }

    /**
     * @return File
     */
    public function file() { }

    /**
     * @return Stream
     */
    public function read() { }

    /**
     * @param string $charset (optional)
     * @return string
     */
    public function readString($charset) { }

    /**
     * @return string binary
     */
    public function readBytes() { }

    /**
     * @param bool $append
     * @param int $bufferSize (optional)
     * @return Stream
     */
    public function write($append, $bufferSize) { }

    /**
     * @param string $string
     * @param bool $append
     * @param string $charset (optional)
     */
    public function writeString($string, $append, $charset) { }

    /**
     * @param string $binaryString
     * @param bool $append
     */
    public function writeBytes($binaryString, $append) { }

    /**
     * @param string $suffix (optional)
     * @return FileHandle[]
     */
    public function getList($suffix) { }

    /**
     * @return bool
     */
    public function isDirectory() { }

    /**
     * @param string $name
     * @return FileHandle
     */
    public function child($name) { }

    /**
     * @param $name
     * @return FileHandle
     */
    public function sibling($name) { }

    /**
     * @return FileHandle
     */
    public function parent() { }

    /**
     * @throws \Exception
     */
    public function mkdirs() { }

    /**
     * @return bool
     */
    public function exists() { }

    /**
     * @return bool
     */
    public function delete() { }

    /**
     * @return bool
     */
    public function deleteDirectory() { }

    /**
     * @param bool $preserveTree (optional)
     */
    public function emptyDirectory($preserveTree) { }

    /**
     * @param FileHandle $fileHandle
     */
    public function copyTo(FileHandle $fileHandle) { }

    /**
     * @param FileHandle $fileHandle
     */
    public function moveTo(FileHandle $fileHandle) { }

    /**
     * @return int
     */
    public function length() { }

    /**
     * @return int
     */
    public function lastModified() { }

    /**
     * @param string $suffix
     * @return FileHandle
     */
    public static function tempFile($suffix) { }

    /**
     * @param string $suffix
     * @return FileHandle
     */
    public static function tempDirectory($suffix) { }
} 