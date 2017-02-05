<?php
namespace php\jsoup;

/**
 * Class ConnectionResponse
 * @package php\jsoup
 */
abstract class ConnectionResponse
{
    const __PACKAGE__ = 'jsoup';

    /**
     * @return array
     */
    public function headers()
    {
    }

    /**
     * @return array
     */
    public function cookies()
    {
    }

    /**
     * Get the status code of the response.
     * @return int
     */
    function statusCode()
    {
    }

    /**
     * Get the status message of the response.
     * @return string
     */
    function statusMessage()
    {
    }

    /**
     * @return string
     */
    function charset()
    {
    }

    /**
     * @return string
     */
    function body()
    {
        return '';
    }

    /**
     * @return string binary string
     */
    function bodyAsBytes()
    {
        return '';
    }

    /**
     * Get the response content type (e.g. "text/html");
     * @return string
     */
    function contentType()
    {
        return '';
    }

    /**
     * @return Document
     */
    function parse()
    {
    }
}