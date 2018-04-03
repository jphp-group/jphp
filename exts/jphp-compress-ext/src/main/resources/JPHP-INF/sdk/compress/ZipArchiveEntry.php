<?php
namespace compress;

use php\io\File;

/**
 * Class TarArchiveEntry
 * @package compress
 * @packages compress
 */
class ZipArchiveEntry extends ArchiveEntry
{
    /**
     * @var int
     */
    public $unixMode;

    /**
     * @var string
     */
    public $comment;

    /**
     * @var int
     */
    public $method;

    /**
     * @var int
     */
    public $crc;

    /**
     * @var int
     */
    public $time;

    /**
     * TarArchiveEntry constructor.
     * @param string $name
     * @param int $size
     */
    function __construct(string $name, int $size = 0)
    {
    }

    /**
     * @param File|string $file
     * @param string $fileName [optional]
     * @return ZipArchiveEntry
     */
    static function ofFile($file, string $fileName): ZipArchiveEntry
    {
    }
}