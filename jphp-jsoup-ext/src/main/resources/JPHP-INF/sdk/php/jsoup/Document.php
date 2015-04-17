<?php
namespace php\jsoup;

/**
 * Class Document
 * @package php\jsoup
 */
abstract class Document
{
    /**
     * @return string
     */
    function location()
    {
        return '';
    }

    /**
     * @param string $value (optional)
     * @return string
     */
    function title($value)
    {
        return '';
    }

    /**
     * @return Element
     */
    function head()
    {
    }

    /**
     * @return Element
     */
    function body()
    {
    }

    /**
     * @param string $cssQuery
     * @return Elements
     */
    function select($cssQuery)
    {
    }
}