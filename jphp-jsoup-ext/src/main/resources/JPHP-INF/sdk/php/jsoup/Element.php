<?php
namespace php\jsoup;

/**
 * Class Element
 * @package php\jsoup
 */
abstract class Element
{
    const __PACKAGE__ = 'jsoup';

    /**
     * @param string $html [optional]
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
     * @param $text [optional]
     * @return string
     */
    function text($text)
    {
        return '';
    }

    /**
     * @return string
     */
    function nodeName()
    {
        return '';
    }

    /**
     * @param string $tagName [optional]
     * @return string
     */
    function tagName($tagName)
    {
        return '';
    }

    /**
     * @return bool
     */
    function isBlock()
    {
        return false;
    }

    /**
     * @return string
     */
    function id()
    {
        return '';
    }

    /**
     * @param $attributeKey
     * @param $attributeValue [optional]
     * @return $this
     */
    function attr($attributeKey, $attributeValue)
    {
    }

    /**
     * @param string $value [optional]
     * @return string
     */
    function val(string $value): string
    {
    }

    /**
     * @return array
     */
    function dataset()
    {
        return [];
    }

    /**
     * @return Element
     */
    function parent()
    {
    }

    /**
     * @return Elements
     */
    function parents()
    {
    }

    /**
     * @param int $index
     * @return Element
     */
    function child($index)
    {
    }

    /**
     * @return Elements
     */
    function children()
    {
    }

    /**
     * @param string $cssQuery
     * @return Elements
     */
    function select($cssQuery)
    {
    }

    /**
     * @param string $class
     */
    function addClass(string $class)
    {
    }

    /**
     * @param string $class
     */
    function removeClass(string $class)
    {
    }

    /**
     * @param string $class
     * @return bool
     */
    function hasClass(string $class): bool
    {
    }
}