<?php
namespace php\compress;

use php\io\File;
use php\io\Stream;

class ZipFile
{
    /**
     * ZipFile constructor.
     * @param $file
     */
    public function __construct($file)
    {
    }

    /**
     * @param ZipFile $file
     * @param string $toDirectory
     * @param string $charset
     * @param callable $callback ($name)
     */
    public static function unpack(ZipFile $file, $toDirectory, $charset = null, callable $callback = null)
    {
    }

    /**
     * @param string $directory
     * @param string $zipFilePath
     * @return ZipFile
     */
    public static function pack($directory, $zipFilePath)
    {
    }

    /**
     * @param callable $reader (array $stat, Stream $stream)
     */
    public function read(callable $reader)
    {
    }

    /**
     * @param string $path
     * @return Stream
     */
    public function get($path)
    {
    }

    /**
     * [name, size, compressedSize, time, crc, comment, method]
     *
     * @param string $path
     * @return array
     */
    public function stat($path)
    {
    }

    /**
     * @return array[]
     */
    public function statAll()
    {
    }

    /**
     * @param $path
     * @return bool
     */
    public function has($path)
    {
    }

    /**
     * @param string $path
     * @param Stream|File|string $source
     * @param int $compressLevel
     */
    public function add($path, $source, $compressLevel = -1)
    {
    }

    /**
     * @param string $path
     * @param string $string
     * @param int $compressLevel
     */
    public function addFromString($path, $string, $compressLevel = -1)
    {
    }

    /**
     * @param array $contents
     * @param int $compressLevel
     */
    public function addAll(array $contents, $compressLevel = -1)
    {
    }

    /**
     * @param string|array $path
     */
    public function remove($path)
    {
    }
}