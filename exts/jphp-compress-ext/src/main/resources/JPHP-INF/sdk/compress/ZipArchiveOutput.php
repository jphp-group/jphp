<?php
namespace compress;

use php\io\File;
use php\io\Stream;

/**
 * @package compress
 * @packages compress
 */
class ZipArchiveOutput extends ArchiveOutput
{
    /**
     * TarArchiveOutput constructor.
     * @param Stream|string|File $output
     */
    public function __construct($output)
    {
    }

    /**
     * @param $file
     * @param string $entryName
     * @return ZipArchiveEntry
     */
    public function createEntry($file, string $entryName)
    {
    }
}