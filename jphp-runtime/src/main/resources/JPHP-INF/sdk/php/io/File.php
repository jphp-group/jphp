<?php
namespace php\io;

use php\format\ProcessorException;

/**
 * Class File
 * @package php\io
 *
 * @packages std, core
 */
class File
{


    /**
     * ``;`` on WINDOWS or ``:`` on UNIX
     */
    const PATH_SEPARATOR = ':';

    /**
     * ``\`` on Windows or ``/`` on Unix
     */
    const DIRECTORY_SEPARATOR = '/';

    /**
     * ``true`` if the names of paths are case insensitive on the current OS
     */
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
     * @see http://docs.oracle.com/javase/7/docs/api/java/nio/file/FileSystem.html#getPathMatcher%28java.lang.String%29
     * @param string $pattern the "glob" and "regex" syntaxes, and may support others.
     *
     * @return bool
     */
    public function matches($pattern) { return false; }

    /**
     * @return bool
     */
    public function delete() { return false; }

    /**
     * @return void
     */
    public function deleteOnExit() { }

    /**
     * @param bool $withDirs
     * @return bool
     */
    public function createNewFile($withDirs = false) { return false; }

    /**
     * @return int
     */
    public function lastModified() { return 0; }

    /**
     * @return int
     */
    public function length() { return 0; }

    /**
     * @return int|null null if not exists or io exception
     */
    public function crc32() { return 0; }

    /**
     * @return string
     */
    public function toUrl() { return ''; }

    /**
     * @param string $algorithm
     * @param callable $onProgress ($sum, $len)
     * @return null|string if not exists or io exception
     */
    public function hash($algorithm = 'MD5', callable $onProgress = null) { return ''; }

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
     * @param callable $filter (File $directory, $name)
     * @return File[]
     * @throws IOException
     */
    public function findFiles(callable $filter = null) { return array(); }

    /**
     * @param string $flags
     * @return Stream
     */
    public function open(string $flags = 'r'): Stream
    {
    }

    /**
     * @param string $format
     * @param int $flags
     * @return mixed
     * @throws IOException
     * @throws ProcessorException
     */
    public function parseAs(string $format, int $flags = 0)
    {
    }

    /**
     * @param mixed $value
     * @param string $format
     * @param int $flags
     * @throws IOException
     * @throws ProcessorException
     */
    public function formatTo($value, string $format, int $flags = 0)
    {
    }

    /**
     * @return string
     */
    public function __toString()
    {
        return $this->getPath();
    }

    /**
     * @param string $prefix
     * @param string $suffix
     * @param null|File|string $directory
     * @return File
     */
    public static function createTemp($prefix, $suffix, $directory = null) { return new File(''); }

    /**
     * List the available filesystem roots.
     * Returns an array of objects denoting the available filesystem roots,
     * or empty array if the set of roots could not be determined.
     * The array will be empty if there are no filesystem roots.
     *
     * @return File[]
     */
    public static function listRoots() { return []; }

    /**
     * @param string $path
     * @return File
     */
    public static function of($path) { }
}
