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
     * --RU--
     * Возвращает символ разделитель для имен файлов на текущей ОС.
     * @return string
     */
    static function separator()
    {
    }

    /**
     * Return the local filesystem's path-separator character.
     * --RU--
     * Возвращает символ разделитель для файловых-путей на текущей ОС.
     * @return string
     */
    static function pathSeparator()
    {
    }

    /**
     * Validate file name.
     * --RU--
     * Проверяет имя файла на корректность.
     * @param $name
     * @return bool
     */
    static function valid($name)
    {
    }

    /**
     * Returns absolute real path.
     * --RU--
     * Возвращает абсолютный путь.
     *
     * @param $path
     * @return string
     */
    static function abs($path)
    {
    }

    /**
     * Returns name of the path.
     * --RU--
     * Возвращает имя файла пути.
     *
     * @param $path
     * @return string
     */
    static function name($path)
    {
    }

    /**
     * Returns name of the path without extension.
     * --RU--
     * Возвращает имя файла пути отсекая расшерение с точкой.
     * @param $path
     * @return string
     */
    static function nameNoExt($path)
    {
    }

    /**
     * Returns path without extension.
     * --RU--
     * Возвращает путь отсекая расшерение с точкой.
     *
     * @param string $path
     * @return string
     */
    static function pathNoExt($path)
    {
    }

    /**
     * Returns extension of path.
     * --RU--
     * Возвращает расширение пути или файла без точки.
     *
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
     * --RU--
     * Возвращает родительскую директорию.
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
     * --RU--
     * Проверяет - есть ли родительские директории для пути и пытается их создать если их нет.
     * См. также: makeDir().
     *
     * @param string $path
     * @return bool
     */
    static function ensureParent($path)
    {
    }

    /**
     * Normalizes file path for current OS.
     * --RU--
     * Приводит файловый путь к родному виду текущей ОС.
     *
     * @param $path
     * @return string
     */
    static function normalize($path)
    {
    }

    /**
     * Checks that file is exists.
     * --RU--
     * Проверяет, существует ли файл.
     *
     * @param $path
     * @return string
     */
    static function exists($path)
    {
    }

    /**
     * Returns size of file in bytes.
     * --RU--
     * Возвращает размер файла в байтах.
     *
     * @param $path
     * @return int
     */
    static function size($path)
    {
    }

    /**
     * Checks that path is file.
     * --RU--
     * Проверяет, является ли указанный путь файлом.
     *
     * @param $path
     * @return bool
     */
    static function isFile($path)
    {
    }

    /**
     * Checks that path is directory.
     * --RU--
     * Проверяет, является ли указанный путь папкой.
     *
     * @param $path
     * @return bool
     */
    static function isDir($path)
    {
    }

    /**
     * Checks that path is hidden.
     * --RU--
     * Проверяет, является ли указанный путь скрытым системой.
     *
     * @param $path
     * @return bool
     */
    static function isHidden($path)
    {
    }

    /**
     * Returns last modification time of file or directory.
     * --RU--
     * Возвращает последнее время модификации пути в unix timestamp (млсек).
     *
     * @param $path
     * @return int
     */
    static function time($path)
    {
    }

    /**
     * Creates empty directory (mkdirs) if not exists.
     * --RU--
     * Создает папку по указаному пути если их еще нет.
     *
     * @param $path
     * @return bool
     */
    static function makeDir($path)
    {
    }

    /**
     * Creates empty file, if file already exists then rewrite it.
     * --RU--
     * Создает пустой файл, если файл уже существует, перезаписывает его.
     *
     * @param $path
     * @return bool
     */
    static function makeFile($path)
    {
    }

    /**
     * Deletes file or empty directory.
     * --RU--
     * Удаляет файл или пустую папку.
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
     * --RU--
     * Удаляет все файлы найденные по указанному пути. Метод не удаляет саму указанную директорию.
     * Возвращает массив с ключами error, success и skip, в которых список файлов.
     *
     * @param string $path
     * @param callable $checker (File $file, $depth) optional, must return true to delete the file.
     * @return array [success => [], error => [], skip = []]
     */
    static function clean($path, callable $checker = null)
    {
    }

    /**
     * Scans the path with callback.
     * --RU--
     * Сканирует директорию с коллбэком.
     *
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
     * --RU--
     * Возвращает хеш файла или потока (stream), по-умолчанию MD5.
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
     * --RU--
     * Копирует из одного файла/потока(stream) в другой файл/поток.
     *
     * @param string|File|Stream $source
     * @param string|File|Stream $dest
     * @param callable $onProgress ($copiedBytes)
     * @param int $bufferSize
     * @return int copied bytes.
     */
    static function copy($source, $dest, callable $onProgress = null, $bufferSize = 8096)
    {
    }

    /**
     * Reads fully data from source and returns it as binary string.
     * --RU--
     * Возвращает данные полученные из потока или файла в виде бинарной строки.
     *
     * @param string $source
     * @param null|string $charset UTF-8, windows-1251, etc.
     * @param string $mode
     * @return string
     */
    static function get($source, $charset = null, $mode = 'r')
    {
    }
}