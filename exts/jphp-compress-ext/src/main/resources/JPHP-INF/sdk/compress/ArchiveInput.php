<?php
namespace compress;

use php\io\Stream;

/**
 * @package compress
 * @packages compress
 */
abstract class ArchiveInput
{
    /**
     * @return Stream
     */
    public function stream(): Stream
    {
    }

    /**
     * @return ArchiveEntry
     */
    abstract public function nextEntry();

    /**
     * @param ArchiveEntry $entry
     * @return bool
     */
    public function canReadEntryData(ArchiveEntry $entry): bool
    {
    }
}