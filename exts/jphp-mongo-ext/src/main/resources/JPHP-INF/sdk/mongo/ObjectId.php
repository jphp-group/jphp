<?php
namespace mongo;

/**
 * Class ObjectId
 * @package mongo
 */
class ObjectId
{
    /**
     * ObjectId constructor.
     * @param string $hexString
     */
    public function __construct(string $hexString)
    {
    }

    /**
     * @return string
     */
    public function __toString(): string
    {
    }

    /**
     * @param string $hexString
     * @return bool
     */
    public static function isValid(string $hexString): bool
    {
    }
}