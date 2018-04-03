<?php
namespace compress;

use php\io\File;
use php\io\IOException;
use php\io\MiscStream;
use php\io\Stream;

/**
 * @package compress
 * @packages compress
 */
class Bzip2OutputStream extends MiscStream
{
    /**
     * GzipOutStream constructor.
     * @param Stream|File|string $output
     * @param int $blockSize
     */
    public function __construct($output, int $blockSize = 9)
    {
    }
}