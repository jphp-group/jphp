<?php
namespace compress;

use php\io\File;
use php\io\Stream;

/**
 * @package compress
 * @packages compress
 */
class ZipArchive extends Archive
{
    /**
     * @return ArchiveInput
     */
    protected function createInput()
    {
        return new ZipArchiveInput($this->source);
    }

    /**
     * @return ArchiveOutput
     */
    protected function createOutput()
    {
        return new ZipArchiveOutput($this->source);
    }

    /**
     * @return ZipArchiveEntry[]
     */
    public function entries(): array
    {
    }

    /**
     * @param string $path
     * @return ZipArchiveEntry
     */
    public function entry(string $path)
    {
    }

    /**
     * @param callable $callback (ZipArchiveEntry $entry, Stream $stream)
     * @return ZipArchiveEntry[]
     */
    public function readAll(callable $callback = null)
    {
    }

    /**
     * @param string $path
     * @param callable $callback (ZipArchiveEntry $entry, Stream $stream)
     * @return ZipArchiveEntry
     */
    public function read(string $path, callable $callback = null)
    {
    }
}