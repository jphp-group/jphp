<?php
namespace php\io;


class File {

    /**
     * @param string $path
     * @param null|string $child
     */
    public function __construct($path, $child = NULL) { }

    /**
     * @return bool
     */
    public function exists() { }

    /**
     * @return bool
     */
    public function canExecute() { }

    /**
     * @return bool
     */
    public function canWrite() { }

    /**
     * @return bool
     */
    public function canRead() { }

    /**
     * @return string
     */
    public function getName() { }

    /**
     * @return string
     */
    public function getAbsolutePath() { }

    /**
     * @return string
     * @throws IOException
     */
    public function getCanonicalPath() { }

    /**
     * @return string
     */
    public function getParent() { }

    /**
     * @return string
     */
    public function getPath() { }

    /**
     * @return File
     */
    public function getAbsoluteFile() { }

    /**
     * @return File
     * @throws IOException
     */
    public function getCanonicalFile() { }

    /**
     * @return File
     */
    public function getParentFile() { }

    /**
     * @return bool
     */
    public function mkdir() { }

    /**
     * @return bool
     */
    public function mkdirs() { }

    /**
     * @return bool
     */
    public function isFile() { }

    /**
     * @return bool
     */
    public function isDirectory() { }

    /**
     * @return bool
     */
    public function isAbsolute() { }

    /**
     * @return bool
     */
    public function isHidden() { }

    /**
     * @return bool
     */
    public function delete() { }

    /**
     * @return void
     */
    public function deleteOnExit() { }

    /**
     * @return bool
     * @throws IOException
     */
    public function createNewFile() { }

    /**
     * @return int
     */
    public function lastModified() { }

    /**
     * @return int
     */
    public function length() { }

    /**
     * @param string $newName
     * @return bool
     */
    public function renameTo($newName) { }

    /**
     * @param bool $value
     * @param bool $ownerOnly
     * @return bool
     */
    public function setExecutable($value, $ownerOnly = true) { }

    /**
     * @param bool $value
     * @param bool $ownerOnly
     * @return bool
     */
    public function setWritable($value, $ownerOnly = true) { }

    /**
     * @param bool $value
     * @param bool $ownerOnly
     * @return bool
     */
    public function setReadable($value, $ownerOnly = true) { }

    /**
     * @return bool
     */
    public function setReadOnly() { }

    /**
     * @param int $time
     * @return bool
     */
    public function setLastModified($time) { }

    /**
     * @param string|File $file
     * @return int
     */
    public function compareTo($file) { }

    /**
     * @param callable $filter
     * @return string[]
     * @throws IOException
     */
    public function find(callable $filter = null) { }
    /**
     * @param callable $filter
     * @return File[]
     * @throws IOException
     */
    public function findFiles(callable $filter = null) { }
}