<?php
namespace php\xml;

/**
 * Class DomNode
 * @package php\xml
 */
abstract class DomNode
{
    /**
     * @param string $xpathExpression
     * @return string
     */
    public function get($xpathExpression)
    {
    }

    /**
     * @param string $xpathExpression
     * @return DomNode
     */
    public function find($xpathExpression)
    {
    }

    /**
     * @param string $xpathExpression
     * @return DomNodeList
     */
    public function findAll($xpathExpression)
    {
    }

    /**
     * @return string
     */
    public function getBaseURI()
    {
    }

    /**
     * @return string
     */
    public function getNamespaceURI()
    {
    }

    /**
     * @return string
     */
    public function getLocalName()
    {
    }

    /**
     * @return int
     */
    public function getNodeType()
    {
    }

    /**
     * @return string
     */
    public function getNodeName()
    {
    }

    /**
     * @return string
     */
    public function getNodeValue()
    {
    }

    /**
     * @return string
     */
    public function getPrefix()
    {
    }

    /**
     * @return string
     */
    public function getTextContent()
    {
    }

    /**
     * @return DomNode
     */
    public function getFirstChild()
    {
    }

    /**
     * @return DomNode
     */
    public function getLastChild()
    {
    }

    /**
     * @return DomNode
     */
    public function getNextSibling()
    {
    }

    /**
     * @return DomNode
     */
    public function getPreviousSibling()
    {
    }

    /**
     * @return DomNode
     */
    public function getParentNode()
    {
    }

    /**
     * @return DomDocument
     */
    public function getOwnerDocument()
    {
    }

    /**
     * @return bool
     */
    public function hasAttributes()
    {
    }

    /**
     * @return bool
     */
    public function hasChildNodes()
    {
    }

    /**
     * @return DomNodeList
     */
    public function getChildNodes()
    {
    }

    /**
     * @param string $namespace
     */
    public function isDefaultNamespace($namespace)
    {
    }

    /**
     * @param DomNode $node
     * @return bool
     */
    public function isEqualNode(DomNode $node)
    {
    }

    /**
     * @param DomNode $node
     * @return bool
     */
    public function isSameNode(DomNode $node)
    {
    }

    /**
     * @param string $feature
     * @param string $version
     * @return bool
     */
    public function isSupported($feature, $version)
    {
    }

    /**
     * @param string $prefix
     * @return string
     */
    public function lookupNamespaceURI($prefix)
    {
    }

    /**
     * @param string $namespaceURI
     * @return string
     */
    public function lookupPrefix($namespaceURI)
    {
    }

    /**
     *
     */
    public function normalize()
    {
    }

    /**
     * @param string $content
     */
    public function setTextContent($content)
    {
    }

    /**
     * @param string $prefix
     */
    public function setPrefix($prefix)
    {
    }

    /**
     * @param bool $deep
     * @return DomNode
     */
    public function cloneNode($deep)
    {
    }

    /**
     * @param DomNode $node
     * @return $this
     */
    public function appendChild(DomNode $node)
    {
    }

    /**
     * @param DomNode $node
     * @return $this
     */
    public function removeChild(DomNode $node)
    {
    }

    /**
     * @param DomNode $newNode
     * @param DomNode $oldNode
     * @return $this
     */
    public function replaceChild(DomNode $newNode, DomNode $oldNode)
    {
    }

    /**
     * @param DomNode $newNode
     * @param DomNode $refNode
     * @return $this
     */
    public function insertBefore(DomNode $newNode, DomNode $refNode)
    {
    }

    /**
     * @return array
     */
    public function toModel() {}
}