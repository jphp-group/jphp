<?php
namespace compress;

use php\io\File;
use php\io\Stream;

/**
 * @package compress
 * @packages compress
 */
class ZipArchiveInput extends ArchiveInput
{
    /**
     * TarArchiveOutput constructor.
     * @param Stream|string|File $output
     * @param string|null $encoding
     */
    public function __construct($output, string $encoding = null)
    {
    }

    /**
     * @return ZipArchiveEntry
     */
    public function nextEntry()
    {
    }
}