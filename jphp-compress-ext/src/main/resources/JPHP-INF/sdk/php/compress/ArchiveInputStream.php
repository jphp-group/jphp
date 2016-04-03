<?php
namespace php\compress;
use php\io\File;
use php\io\Stream;
use php\io\MiscStream;

/**
 * jphp-compress-ext
 *
 * Class ArchiveInputStream for reading archive
 * @package php\compress
 */
class ArchiveInputStream extends MiscStream
{
    /**
     * @param string $format zip, tar, jar
     * @param File|Stream $source
     */
    public function __construct($format, $source)
    {
    }

    /**
     * @return ArchiveEntry
     */
    public function nextEntry()
    {
    }
}