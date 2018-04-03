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
class Lz4InputStream extends MiscStream
{
    /**
     * @param Stream|File|string $input
     * @param bool $framed
     */
    public function __construct($input, bool $framed = false)
    {
    }
}