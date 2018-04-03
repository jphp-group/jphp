<?php
namespace compress;

use php\io\File;
use php\io\IOException;
use php\io\MiscStream;
use php\io\Stream;

/**
 * Class GzipOutStream
 * @package compress
 * @packages compress
 */
class GzipOutputStream extends MiscStream
{
    /**
     * GzipOutStream constructor.
     * @param Stream|File|string $output
     * @param array $parameters [comment, filename, compressionLevel, modificationTime, operatingSystem]
     */
    public function __construct($output, array $parameters = null)
    {
    }
}