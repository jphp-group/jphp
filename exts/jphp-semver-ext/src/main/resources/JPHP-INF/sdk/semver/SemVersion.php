<?php
namespace semver;

/**
 * Class SemVersion
 * @package semver
 */
class SemVersion
{
    /**
     * SemVersion constructor.
     * @param string $value
     */
    public function __construct(string $value)
    {
    }

    /**
     * @return bool
     */
    public function isStable(): bool
    {
    }

    /**
     * @return int
     */
    public function getMajorNum(): int
    {
    }

    /**
     * @return int
     */
    public function getMinorNum(): int
    {
    }

    /**
     * @return int
     */
    public function getPatchNum(): int
    {
    }

    /**
     * @return string
     */
    public function getPreReleaseString(): string
    {
    }


    /**
     * @return string
     */
    public function toNormal(): string
    {
    }

    /**
     * @return string
     */
    public function __toString(): string
    {
        return '';
    }

    /**
     * @return SemVersion
     */
    public function incMajorNum(): SemVersion
    {
    }

    /**
     * @return SemVersion
     */
    public function incMinorNum(): SemVersion
    {
    }

    /**
     * @return SemVersion
     */
    public function incPatchNum(): SemVersion
    {
    }


    /**
     * @param string $expr
     * @return bool
     */
    public function satisfies(string $expr): bool
    {
    }
}