<?php
namespace compress;

use php\io\File;
use php\io\Stream;

/**
 * Class TarArchive
 * @package compress
 * @packages compress
 */
class TarArchive extends Archive
{
    /**
     * @return ArchiveInput
     */
    protected function createInput()
    {
        return new TarArchiveInput($this->source);
    }

    /**
     * @return ArchiveOutput
     */
    protected function createOutput()
    {
        return new TarArchiveOutput($this->source);
    }

    /**
     * @return TarArchiveEntry[]
     */
    public function entries(): array
    {
    }

    /**
     * @param string $path
     * @return TarArchiveEntry
     */
    public function entry(string $path)
    {
    }

    /**
     * @param callable $callback (TarArchiveEntry $entry, Stream $stream)
     * @return TarArchiveEntry[]
     */
    public function readAll(callable $callback = null)
    {
    }

    /**
     * @param string $path
     * @param callable $callback (TarArchiveEntry $entry, Stream $stream)
     * @return TarArchiveEntry
     */
    public function read(string $path, callable $callback = null)
    {
    }
}