<?php
namespace compress;

use php\io\File;
use php\io\Stream;

/**
 * Class TarArchiveOutputStream
 * @package compress
 * @packages compress
 */
class TarArchiveOutput extends ArchiveOutput
{
    const BLOCK_SIZE_UNSPECIFIED = -511;

    /**
     * TarArchiveOutput constructor.
     * @param Stream|string|File $output
     * @param int $blockSize
     * @param string|null $encoding
     */
    public function __construct($output, int $blockSize = self::BLOCK_SIZE_UNSPECIFIED, string $encoding = null)
    {
    }

    /**
     * @param $file
     * @param string $entryName
     * @return TarArchiveEntry
     */
    public function createEntry($file, string $entryName)
    {
    }
}