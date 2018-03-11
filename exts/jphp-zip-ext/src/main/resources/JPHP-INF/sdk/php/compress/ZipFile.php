<?php
namespace php\compress;

use php\io\File;
use php\io\Stream;

/**
 * Class ZipFile
 * @package php\compress
 */
class ZipFile
{
    /**
     * Create ZIP Archive.
     *
     * @param string $file
     * @param bool $rewrite
     * @return ZipFile
     */
    public static function create($file, $rewrite = true)
    {
    }

    /**
     * ZipFile constructor.
     * @param $file
     * @param bool $create
     */
    public function __construct($file, $create = false)
    {
    }

    /**
     * Extract zip archive content to directory.
     *
     * @param string $toDirectory
     * @param string $charset
     * @param callable $callback ($name)
     */
    public function unpack($toDirectory, $charset = null, callable $callback = null)
    {
    }

    /**
     * Read one zip entry from archive.
     * @param string $path
     * @param callable $reader (array $stat, Stream $stream)
     */
    public function read($path, callable $reader)
    {
    }

    /**
     * Read all zip entries from archive.
     * @param callable $reader (array $stat, Stream $stream)
     */
    public function readAll(callable $reader)
    {
    }

    /**
     * Returns stat of one zip entry by path.
     * [name, size, compressedSize, time, crc, comment, method, directory]
     *
     * @param string $path
     * @return array
     */
    public function stat($path)
    {
    }

    /**
     * Returns all stats of zip archive.
     * @return array[]
     */
    public function statAll()
    {
    }

    /**
     * Checks zip entry exist by path.
     * @param $path
     * @return bool
     */
    public function has($path)
    {
    }

    /**
     * Add stream or file to archive.
     *
     * @param string $path
     * @param Stream|File|string $source
     * @param int $compressLevel
     */
    public function add($path, $source, $compressLevel = -1)
    {
    }

    /**
     * Add all files of directory to archive.
     *
     * @param string $dir
     * @param int $compressLevel
     * @param callable $callback
     */
    public function addDirectory($dir, $compressLevel = -1, callable $callback = null)
    {
    }

    /**
     * Add zip entry from string.
     * @param string $path
     * @param string $string
     * @param int $compressLevel
     */
    public function addFromString($path, $string, $compressLevel = -1)
    {
    }

    /**
     * Remove zip entry by its path.
     * @param string|array $path
     */
    public function remove($path)
    {
    }

    /**
     * @return string
     */
    public function getPath()
    {
    }
}