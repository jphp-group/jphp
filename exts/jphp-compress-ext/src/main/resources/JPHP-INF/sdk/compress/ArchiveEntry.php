<?php
namespace compress;

use php\time\Time;

/**
 * Class ArchiveEntry
 * @package compress
 * @packages compress
 */
abstract class ArchiveEntry
{
    /**
     * @var string
     */
    public $name;

    /**
     * @var int
     */
    public $size;

    /**
     * @readonly
     * @var Time
     */
    public $lastModifiedDate;

    /**
     * @return bool
     */
    public function isDirectory(): bool
    {
    }
}