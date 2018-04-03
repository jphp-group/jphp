<?php
namespace compress;

use php\io\File;

/**
 * Class TarArchiveEntry
 * @package compress
 * @packages compress
 */
class TarArchiveEntry extends ArchiveEntry
{
    /**
     * @var int
     */
    public $userId;

    /**
     * @var string
     */
    public $userName;

    /**
     * @var int
     */
    public $groupId;

    /**
     * @var string
     */
    public $groupName;

    /**
     * @var string
     */
    public $linkName;

    /**
     * @var int
     */
    public $mode;

    /**
     * @var int
     */
    public $modTime;

    /**
     * @readonly
     * @var int
     */
    public $realSize;

    /**
     * @var int
     */
    public $devMinor;

    /**
     * @var int
     */
    public $devMajor;

    /**
     * @return bool
     */
    function isCheckSumOK(): bool { }

    /**
     * @return bool
     */
    function isBlockDevice(): bool { }

    /**
     * @return bool
     */
    function isFIFO(): bool { }

    /**
     * @return bool
     */
    function isSparse(): bool { }

    /**
     * @return bool
     */
    function isCharacterDevice(): bool { }

    /**
     * @return bool
     */
    function isLink(): bool { }

    /**
     * @return bool
     */
    function isSymbolicLink(): bool { }

    /**
     * @return bool
     */
    function isFile(): bool { }

    /**
     * @return bool
     */
    function isGlobalPaxHeader(): bool { }

    /**
     * @return bool
     */
    function isPaxHeader(): bool { }

    /**
     * @return bool
     */
    function isGNULongNameEntry(): bool { }

    /**
     * @return bool
     */
    function isGNULongLinkEntry(): bool { }

    /**
     * @return bool
     */
    function isStarSparse(): bool { }

    /**
     * @return bool
     */
    function isPaxGNUSparse(): bool { }

    /**
     * @return bool
     */
    function isOldGNUSparse(): bool { }

    /**
     * @return bool
     */
    function isGNUSparse(): bool { }

    /**
     * @return bool
     */
    function isExtended(): bool { }

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
     * @return TarArchiveEntry
     */
    static function ofFile($file, string $fileName): TarArchiveEntry
    {
    }

    /**
     * @param string $name
     * @param string $value
     */
    function addPaxHeader(string $name, string $value)
    {
    }

    /**
     *
     */
    function clearExtraPaxHeaders(): void
    {
    }

    /**
     * @param string $name
     * @return string
     */
    function getExtraPaxHeader(string $name): string
    {
    }

    /**
     * @return array
     */
    function getExtraPaxHeaders(): array
    {
    }
}