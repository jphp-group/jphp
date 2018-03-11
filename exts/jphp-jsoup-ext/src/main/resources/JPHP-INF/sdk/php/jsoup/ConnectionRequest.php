<?php
namespace php\jsoup;

/**
 * Class ConnectionRequest
 * @package php\jsoup
 */
abstract class ConnectionRequest
{
    const __PACKAGE__ = 'jsoup';

    /**
     * Setter and getter for timeout.
     *
     * @param int $millis (optional)
     * @return int|ConnectionRequest
     */
    function timeout($millis)
    {
    }

    /**
     * Setter and getter for max of body size.
     * @param int $bytes (optional)
     * @return int|ConnectionRequest
     */
    function maxBodySize($bytes)
    {
    }

    /**
     * Setter and getter.
     * @param bool $enable (optional)
     * @return bool|ConnectionRequest
     */
    function followRedirects($enable)
    {
    }

    /**
     * @param bool $enable (optional)
     * @return bool|ConnectionRequest
     */
    function ignoreHttpErrors($enable)
    {
    }

    /**
     * @param bool $enable (optional)
     * @return bool|ConnectionRequest
     */
    function ignoreContentType($enable)
    {
    }
}