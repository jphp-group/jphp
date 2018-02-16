<?php
namespace php\lib;
use php\format\ProcessorException;
use php\io\File;
use php\io\IOException;
use php\io\Stream;


/**
 * File System class.
 *
 * Class fs
 * @packages std, core
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
     *
     * Array filter, e.g.:
     * [
     *      namePattern => string (regex),
     *      extensions => [...],
     *      excludeExtensions => [...],
     *      excludeDirs => bool,
     *      excludeFiles => bool,
     *      excludeHidden => bool,
     *
     *      minSize => int
     *      maxSize => int
     *      minTime => int, millis
     *      maxTime => int, millis
     *
     *      callback => function (File $file, $depth) { }
     * ]
     *
     * --RU--
     * Удаляет все файлы найденные по указанному пути. Метод не удаляет саму указанную директорию.
     * Возвращает массив с ключами error, success и skip, в которых список файлов.
     *
     * Фильтр может быть в виде массива:
     *  [
     *      namePattern => string (regex),
     *      extensions => [...],
     *      excludeExtensions => [...],
     *      excludeDirs => bool,
     *      excludeFiles => bool,
     *      excludeHidden => bool,
     *
     *      minSize => int (мин. размер файла, включительно)
     *      maxSize => int (макс. размер файла, включительно),
     *      minTime => int, millis (мин. время изменения файла, включительно)
     *      maxTime => int, millis (макс. время изменения файла, включительно)
     *
     *      callback => function (File $file, $depth) { }
     *  ]
     *
     * @param string $path
     * @param callable|array $filter (File $file, $depth) optional, must return true to delete the file.
     * @return array [success => [], error => [], skip = []]
     */
    static function clean($path, $filter = null)
    {
    }

    /**
     * Scans the path with callback or array filter and can returns found list
     * if the callback returns any result or if the callback is null.
     *
     * Array filter, e.g.:
     * [
     *      namePattern => string (regex),
     *      extensions => [...],
     *      excludeExtensions => [...],
     *      excludeDirs => bool,
     *      excludeFiles => bool,
     *      excludeHidden => bool,
     *
     *      minSize => int
     *      maxSize => int
     *      minTime => int, millis
     *      maxTime => int, millis
     *
     *      callback => function (File $file, $depth) { }
     * ]
     *
     * --RU--
     * Сканирует директорию с коллбэком или фильтром, и может возвращать список найденного, если
     * из коллбэка возвращать результат или если коллбэк не передан.
     *
     * Фильтр в виде массива:
     *  [
     *      namePattern => string (regex),
     *      extensions => [...],
     *      excludeExtensions => [...],
     *      excludeDirs => bool,
     *      excludeFiles => bool,
     *      excludeHidden => bool,
     *
     *      minSize => int (мин. размер файла, включительно)
     *      maxSize => int (макс. размер файла, включительно),
     *      minTime => int, millis (мин. время изменения файла, включительно)
     *      maxTime => int, millis (макс. время изменения файла, включительно)
     *
     *      callback => function (File $file, $depth) { }
     *  ]
     *
     * @param string $path
     * @param callable|array $filter (File $file, $depth): mixed|null
     * @param int $maxDepth if 0 then unlimited.
     * @param bool $subIsFirst
     * @return array[]
     */
    static function scan($path, $filter = null, $maxDepth = 0, $subIsFirst = false)
    {
    }

    /**
     * Calculates hash of file or stream.
     * --RU--
     * Возвращает хеш файла или потока (stream), по-умолчанию MD5.
     *
     * @param string|Stream $source
     * @param string $algo MD5, MD2, SHA-1, SHA-256, SHA-512
     * @param callable $onProgress ($sum, $readBytes)
     * @return string
     */
    static function hash($source, $algo = 'MD5', callable $onProgress = null)
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
     * Renames or moves a file or empty dir.
     * --RU--
     * Переименновывает или перемещает файл, либо пустую папку.
     *
     * @param string $fromPath
     * @param string $toPath
     */
    static function move($fromPath, $toPath)
    {
    }

    /**
     * Set name for file, returns true if success.
     * --RU--
     * Задает файлу новое название, возвращает true при успехе.
     *
     * @param string $pathToFile
     * @param string $newName
     */
    static function rename($pathToFile, $newName)
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

    /**
     * Read fully data from source, parse as format and return result.
     * --RU--
     * Читает данные в переданном формате из источника и возвращает результат.
     *
     * @param $path
     * @param string $format json, xml, yaml, etc.
     * @param int $flags
     * @return mixed
     * @throws ProcessorException
     * @throws IOException
     */
    static function parseAs($path, string $format, int $flags = -1)
    {
    }

    /**
     * Read fully data from source, parse as format by extensions and return result.
     * --RU--
     * Читает данные в формате на основе расширения пути из источника и возвращает результат.
     *
     * @param $path
     * @param int $flags
     * @return mixed
     * @throws ProcessorException
     * @throws IOException
     */
    static function parse($path, int $flags = -1)
    {
    }

    /**
     * Write formatted data to source (path).
     * --RU--
     * Записывает данные в нужном формате.
     *
     * @param $path
     * @param mixed $value
     * @param string $format
     * @param int $flags
     */
    static function formatAs($path, $value, string $format, int $flags = -1)
    {
    }

    /**
     * Write formatted (based on path extension) data to source (path).
     * --RU--
     * Записывает данные в нужном формате на основе расширения.
     *
     * @param $path
     * @param mixed $value
     * @param int $flags
     */
    static function format($path, $value, int $flags = -1)
    {
    }
}