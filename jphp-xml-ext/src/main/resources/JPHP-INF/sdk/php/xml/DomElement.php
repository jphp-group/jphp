<?php
namespace php\xml;
use Traversable;

/**
 * Class DomElement
 * @package php\xml
 */
abstract class DomElement extends DomNode
{
    /**
     * @param string $name
     * @return string Value of attribute by $name
     */
    public function __get($name) {}

    /**
     * Set attribute value
     * @param string $name
     * @param string $value
     */
    public function __set($name, $value) {}

    /**
     * Remove attribute by name
     * @param string $name
     */
    public function __unset($name) {}

    /**
     * Check attribute exists by name
     * @param $name
     * @return bool
     */
    public function __isset($name) {}

    /**
     * @return string
     */
    public function getTagName()
    {
    }

    /**
     * @param string $name
     * @return string
     */
    public function getAttribute($name)
    {
    }

    /**
     * @param string $name
     * @return bool
     */
    public function hasAttribute($name)
    {
    }

    /**
     * @param string $namespaceURI
     * @param string $localName
     * @return bool
     */
    public function hasAttributeNS($namespaceURI, $localName)
    {
    }

    /**
     * @param string $name
     * @param string $value
     */
    public function setAttribute($name, $value)
    {
    }

    /**
     * @param array|Traversable $attributes
     */
    public function setAttributes($attributes)
    {
    }

    /**
     * @return array
     */
    public function getAttributes()
    {
    }

    /**
     * @param string $name
     */
    public function removeAttribute($name)
    {
    }

    /**
     * @param string $name
     * @return DomNodeList
     */
    public function getElementsByTagName($name)
    {
    }

    /**
     * @param string $namespaceURI
     * @param string $localName
     * @return DomNodeList
     */
    public function getElementsByTagNameNS($namespaceURI, $localName)
    {
    }

    /**
     * @param string $namespaceURI
     * @param string $localName
     * @return string
     */
    public function getAttributeNS($namespaceURI, $localName)
    {
    }

    /**
     * @param string $namespaceURI
     * @param string $qualifiedName
     * @param string $value
     */
    public function setAttributeNS($namespaceURI, $qualifiedName, $value)
    {
    }

    /**
     * @param string $namespaceURI
     * @param string $localName
     */
    public function removeAttributeNS($namespaceURI, $localName)
    {
    }

    /**
     * @param string $name
     * @param bool $isId
     */
    public function setIdAttribute($name, $isId)
    {
    }

    /**
     * @param string $namespaceURI
     * @param string $localName
     * @param string $isId
     */
    public function setIdAttributeNS($namespaceURI, $localName, $isId)
    {
    }
}