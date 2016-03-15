<?php
namespace php\lib;
use php\io\File;
use php\io\Stream;


/**
 * File System class.
 *
 * Class fs
 * @package php\lib
 */
class fs
{
    /**
     * Return the local filesystem's name-separator character.
     * @return string
     */
    static function separator()
    {
    }

    /**
     * Return the local filesystem's path-separator character.
     * @return string
     */
    static function pathSeparator()
    {
    }

    /**
     * Returns absolute real path.
     *
     * @param $path
     * @return string
     */
    static function abs($path)
    {
    }

    /**
     * @param $path
     * @return string
     */
    static function name($path)
    {
    }

    /**
     * @param $path
     * @return string
     */
    static function nameNoExt($path)
    {
    }

    /**
     * Returns path without extension.
     *
     * @param string $path
     * @return string
     */
    static function pathNoExt($path)
    {
    }

    /**
     * Returns extension of path.
     * @param $path
     * @return string
     */
    static function ext($path)
    {
    }

    /**
     * Check that $path has an extension from the extension set.
     * @param string $path
     * @param string|array $extensions
     * @param bool $ignoreCase
     * @return bool
     */
    static function hasExt($path, $extensions = null, $ignoreCase = true)
    {
    }

    /**
     * Returns parent directory.
     *
     * @param $path
     * @return string
     */
    static function parent($path)
    {
    }

    /**
     * Checks parent of path and if it is not exists, tries to create parent directory.
     * See makeDir().
     *
     * @param string $path
     * @return bool
     */
    static function ensureParent($path)
    {
    }

    /**
     * Normalizes file path for current OS.
     *
     * @param $path
     * @return string
     */
    static function normalize($path)
    {
    }
    /**
     * @param $path
     * @return string
     */
    static function exists($path)
    {
    }

    /**
     * Returns size of file in bytes.
     *
     * @param $path
     * @return int
     */
    static function size($path)
    {
    }

    /**
     * @param $path
     * @return bool
     */
    static function isFile($path)
    {
    }

    /**
     * @param $path
     * @return bool
     */
    static function isDir($path)
    {
    }

    /**
     * @param $path
     * @return bool
     */
    static function isHidden($path)
    {
    }

    /**
     * Returns last modification time of file or directory.
     *
     * @param $path
     * @return int
     */
    static function time($path)
    {
    }

    /**
     * Creates empty directory (mkdirs) if not exists.
     *
     * @param $path
     * @return bool
     */
    static function makeDir($path)
    {
    }

    /**
     * Creates empty file, if file already exists then rewrite it.
     *
     * @param $path
     * @return bool
     */
    static function makeFile($path)
    {
    }

    /**
     * Deletes file or empty directory.
     *
     * @param $path
     * @return bool
     */
    static function delete($path)
    {
    }

    /**
     * Deletes all files in path. This method does not delete the $path directory.
     * Returns array with error, success and skip file list.
     *
     * @param string $path
     * @param callable $checker (File $file, $depth) optional, must return true to delete the file.
     * @return array [success => [], error => [], skip = []]
     */
    static function clean($path, callable $checker = null)
    {
    }

    /**
     * @param string $path
     * @param callable $onProgress (File $file, $depth)
     * @param int $maxDepth if 0 then unlimited.
     * @param bool $subIsFirst
     */
    static function scan($path, callable $onProgress, $maxDepth = 0, $subIsFirst = false)
    {
    }

    /**
     * Calculates hash of file or stream.
     *
     * @param string|Stream $source
     * @param string $algo MD5, MD2, SHA-1, SHA-256, SHA-512
     * @return string
     */
    static function hash($source, $algo = 'MD5')
    {
    }

    /**
     * Copies $source stream to $dest stream.
     *
     * @param string|File|Stream $source
     * @param string|File|Stream $dest
     * @param callable $onProgress ($copiedBytes)
     * @return int copied bytes.
     */
    static function copy($source, $dest, callable $onProgress = null)
    {
    }

    /**
     * Reads fully data from source and returns it as binary string.
     * @param string $source
     * @param null|string $charset UTF-8, windows-1251, etc.
     * @param string $mode
     * @return string
     */
    static function get($source, $charset = null, $mode = 'r')
    {
    }
}