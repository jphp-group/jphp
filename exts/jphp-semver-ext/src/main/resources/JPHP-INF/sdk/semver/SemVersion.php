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
    public function getBuildString(): string
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
     * @param string $preRelease [optional]
     * @return SemVersion
     */
    public function incMajorNum(string $preRelease): SemVersion
    {
    }

    /**
     * @param string $preRelease [optional]
     * @return SemVersion
     */
    public function incMinorNum(string $preRelease): SemVersion
    {
    }

    /**
     * @param string $preRelease [optional]
     * @return SemVersion
     */
    public function incPatchNum(string $preRelease): SemVersion
    {
    }

    /**
     * @return SemVersion
     */
    public function incBuildString(): SemVersion
    {
    }

    /**
     * @return SemVersion
     */
    public function incPreRelease(): SemVersion
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