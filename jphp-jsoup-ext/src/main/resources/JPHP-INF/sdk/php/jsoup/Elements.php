<?php
namespace php\jsoup;

use Iterator;

/**
 * Class Elements
 * @package php\jsoup
 */
abstract class Elements implements Iterator
{
    const __PACKAGE__ = 'jsoup';

    /**
     * @return string
     */
    function text()
    {
        return '';
    }

    /**
     * @return bool
     */
    function hasText()
    {
        return false;
    }

    /**
     * @param string $html (optional)
     * @return string
     */
    function html($html)
    {
        return '';
    }

    /**
     * @return string
     */
    function outerHtml()
    {
        return '';
    }

    /**
     * @param string $attributeKey
     * @param $value (optional)
     * @return string|$this
     */
    function attr($attributeKey, $value)
    {
        return '';
    }

    /**
     * @param string $attributeKey
     * @return bool
     */
    function hasAttr($attributeKey)
    {
        return false;
    }

    /**
     * @param string $attributeKey
     * @return Elements
     */
    function removeAttr($attributeKey)
    {
    }

    /**
     * @param string $class
     * @return Elements
     */
    function addClass($class)
    {
    }

    /**
     * @param string $class
     * @return Elements
     */
    function removeClass($class)
    {
    }

    /**
     * @param string $class
     * @return bool
     */
    function hasClass($class)
    {
    }

    /**
     * @param string $class
     * @return bool
     */
    function toggleClass($class)
    {
    }

    /**
     * @param string $value (optional)
     * @return $this
     */
    function val($value)
    {
    }

    /**
     * @param string $html
     * @return Elements
     */
    function prepend($html)
    {
    }

    /**
     * @param string $html
     * @return Elements
     */
    function append($html)
    {
    }

    /**
     * @param string $html
     * @return Elements
     */
    function before($html)
    {
    }

    /**
     * @param string $html
     * @return Elements
     */
    function after($html)
    {
    }

    /**
     * @param string $query
     * @return Elements
     */
    function select($query)
    {
    }

    /**
     * @return Element
     */
    function first()
    {
    }

    /**
     * @return Element
     */
    function last()
    {
    }

    /**
     * @param string $query
     * @return Elements
     */
    function not($query)
    {
    }

    /**
     * @param string $query
     * @return bool
     */
    function is($query)
    {
        return false;
    }

    /**
     * @return Elements
     */
    function parents()
    {
    }
}