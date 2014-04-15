<?php
namespace php\io;

/**
 * Class File
 * @package php\io
 */
class File {

    const PATH_SEPARATOR = ':'; // ; for WINDOWS
    const DIRECTORY_SEPARATOR = '/'; // \ for WINDOWS
    const PATH_NAME_CASE_INSENSITIVE = false; // true in WINDOWS

    /**
     * @param string $path
     * @param null|string $child
     */
    public function __construct($path, $child = NULL) { }

    /**
     * @return bool
     */
    public function exists() { return false; }

    /**
     * @return bool
     */
    public function canExecute() { return false; }

    /**
     * @return bool
     */
    public function canWrite() { return false; }

    /**
     * @return bool
     */
    public function canRead() { return false; }

    /**
     * @return string
     */
    public function getName() { return ''; }

    /**
     * @return string
     */
    public function getAbsolutePath() { return ''; }

    /**
     * @return string
     * @throws IOException
     */
    public function getCanonicalPath() { return ''; }

    /**
     * @return string
     */
    public function getParent() { return ''; }

    /**
     * @return string
     */
    public function getPath() { return ''; }

    /**
     * @return File
     */
    public function getAbsoluteFile() { return new File(''); }

    /**
     * @return File
     * @throws IOException
     */
    public function getCanonicalFile() { return new File(''); }

    /**
     * @return File
     */
    public function getParentFile() { return new File(''); }

    /**
     * @return bool
     */
    public function mkdir() { return false; }

    /**
     * @return bool
     */
    public function mkdirs() { return false; }

    /**
     * @return bool
     */
    public function isFile() { return false; }

    /**
     * @return bool
     */
    public function isDirectory() { return false; }

    /**
     * @return bool
     */
    public function isAbsolute() { return false; }

    /**
     * @return bool
     */
    public function isHidden() { return false; }

    /**
     * @return bool
     */
    public function delete() { return false; }

    /**
     * @return void
     */
    public function deleteOnExit() { }

    /**
     * @return bool
     * @throws IOException
     */
    public function createNewFile() { return false; }

    /**
     * @return int
     */
    public function lastModified() { return 0; }

    /**
     * @return int
     */
    public function length() { return 0; }

    /**
     * @param string $newName
     * @return bool
     */
    public function renameTo($newName) { return false; }

    /**
     * @param bool $value
     * @param bool $ownerOnly
     * @return bool
     */
    public function setExecutable($value, $ownerOnly = true) { return false; }

    /**
     * @param bool $value
     * @param bool $ownerOnly
     * @return bool
     */
    public function setWritable($value, $ownerOnly = true) { return false; }

    /**
     * @param bool $value
     * @param bool $ownerOnly
     * @return bool
     */
    public function setReadable($value, $ownerOnly = true) { return false; }

    /**
     * @return bool
     */
    public function setReadOnly() { return false; }

    /**
     * @param int $time
     * @return bool
     */
    public function setLastModified($time) { return false; }

    /**
     * @param string|File $file
     * @return int
     */
    public function compareTo($file) { return 0; }

    /**
     * @param callable $filter
     * @return string[]
     * @throws IOException
     */
    public function find(callable $filter = null) { return array(); }
    /**
     * @param callable $filter
     * @return File[]
     * @throws IOException
     */
    public function findFiles(callable $filter = null) { return array(); }

    /**
     * @param string $prefix
     * @param string $suffix
     * @param null|File|string $directory
     * @return File
     */
    public static function createTemp($prefix, $suffix, $directory = null) { return new File(''); }

    /**
     * List the available filesystem roots.
     *   @see java.io.File.listRoots()
     *
     * @return File[]
     *          An array of {@code File} objects denoting the available
     *          filesystem roots, or empty array if the set of roots could not
     *          be determined.  The array will be empty if there are no
     *          filesystem roots.
     */
    public static function listRoots() { return []; }
}
