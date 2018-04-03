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
class Bzip2InputStream extends MiscStream
{
    /**
     * GzipOutStream constructor.
     * @param Stream|File|string $input
     * @param bool $decompressConcatenated
     */
    public function __construct($input, bool $decompressConcatenated = false)
    {
    }
}