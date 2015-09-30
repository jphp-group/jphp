<?php
namespace php\compress;

use php\io\File;
use php\io\MiscStream;
use php\io\Stream;

/**
 * Class ArchiveOutputStream for creating archives
 * @package php\compress
 */
class ArchiveOutputStream extends MiscStream
{
    /**
     * @param string $format zip, tar, jar, ar
     * @param File|Stream $source
     */
    public function __construct($format, $source)
    {
    }

    /**
     * @param string $file
     * @param string $name
     * @return ArchiveEntry
     */
    public function createEntry($file, $name)
    {
    }

    /**
     * @param string $file
     * @param string $name
     * @return ArchiveEntry
     */
    public function addFile($file, $name)
    {
    }

    /**
     * @param ArchiveEntry $entry
     */
    public function addEntry(ArchiveEntry $entry)
    {
    }

    /**
     * @param ArchiveEntry $entry
     */
    public function canAddEntry(ArchiveEntry $entry)
    {
    }

    public function closeEntry()
    {
    }
}