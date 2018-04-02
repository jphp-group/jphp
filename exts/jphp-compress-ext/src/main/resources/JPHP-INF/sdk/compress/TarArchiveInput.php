<?php
namespace compress;

use php\io\File;
use php\io\Stream;

/**
 * @package compress
 * @packages compress
 */
class TarArchiveInput extends ArchiveInput
{
    /**
     * TarArchiveOutput constructor.
     * @param Stream|string|File $output
     * @param int $blockSize
     * @param string|null $encoding
     */
    public function __construct($output, int $blockSize = 512, string $encoding = null)
    {
    }

    /**
     * @return TarArchiveEntry
     */
    public function nextEntry()
    {
    }
}