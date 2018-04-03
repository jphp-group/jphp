<?php
namespace compress;

use php\io\File;
use php\io\IOException;
use php\io\MemoryStream;
use php\io\Stream;

/**
 * Class Archive
 * @package compress
 */
abstract class Archive
{
    /**
     * @readonly
     * @var string|Stream|File
     */
    public $source;

    /**
     * Archive constructor.
     * @param File|Stream|string $source
     */
    public function __construct($source)
    {
        $this->source = $source;
    }

    /**
     * @return ArchiveInput
     */
    abstract protected function createInput();

    /**
     * @return ArchiveOutput
     */
    abstract protected function createOutput();

    /**
     * Open archive for writing.
     * @throws IOException
     */
    public function open()
    {
    }

    /**
     * Finish writing archive.
     * @throws IOException
     */
    public function close()
    {
    }


    /**
     * @return ArchiveEntry[]
     */
    public function entries(): array
    {
    }

    /**
     * @param string $path
     * @return ArchiveEntry
     */
    public function entry(string $path)
    {
    }

    /**
     * @param callable $callback (TarArchive $entry, Stream $stream)
     * @return ArchiveEntry[]
     */
    public function readAll(callable $callback = null)
    {
    }

    /**
     * @param string $path
     * @param callable $callback (TarArchive $entry, Stream $stream)
     * @return ArchiveEntry
     */
    public function read(string $path, callable $callback = null)
    {
    }

    /**
     * @param File|string $path
     * @param string|null $localName
     * @param callable $progressHandler (int $copiedBytes, int $readCount)
     * @param int $bufferSize
     */
    public function addFile($path, string $localName = null, callable $progressHandler = null, int $bufferSize = 8192)
    {
    }

    /**
     * @param ArchiveEntry $entry
     * @param string $contents
     */
    public function addFromString(ArchiveEntry $entry, string $contents)
    {
    }

    /**
     * @param ArchiveEntry $entry
     * @param string|Stream|File|null $source
     * @param callable $progressHandler
     * @param int $bufferSize
     */
    public function addEntry(ArchiveEntry $entry, $source, callable $progressHandler = null, int $bufferSize = 8192)
    {
    }

    /**
     * @param ArchiveEntry $entry
     */
    public function addEmptyEntry(ArchiveEntry $entry)
    {
    }
}