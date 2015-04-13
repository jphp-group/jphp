<?php
namespace php\jsoup;

/**
 * Class Elements
 * @package php\jsoup
 */
class Elements implements \Traversable {
    /**
     * @return string
     */
    function text() { return ''; }

    /**
     * @return bool
     */
    function hasText() { return false; }

    /**
     * @param string $html (optional)
     * @return string
     */
    function html($html) { return ''; }

    /**
     * @return string
     */
    function outerHtml() { return ''; }

    /**
     * @param string $attributeKey
     * @return string
     */
    function attr($attributeKey) { return ''; }

    /**
     * @param string $attributeKey
     * @return bool
     */
    function hasAttr($attributeKey) { return false; }

    /**
     * @param string $value (optional)
     * @return $this
     */
    function val($value) { }

    /**
     * @param string $html
     * @return Elements
     */
    function prepend($html) {  }

    /**
     * @param string $html
     * @return Elements
     */
    function append($html) {  }

    /**
     * @param string $html
     * @return Elements
     */
    function before($html) {  }

    /**
     * @param string $html
     * @return Elements
     */
    function after($html) {  }

    /**
     * @param string $query
     * @return Elements
     */
    function select($query) { }

    /**
     * @param string $query
     * @return Elements
     */
    function not($query) { }

    /**
     * @param string $query
     * @return bool
     */
    function is($query) { return false; }

    /**
     * @return Elements
     */
    function parents() {  }
} 