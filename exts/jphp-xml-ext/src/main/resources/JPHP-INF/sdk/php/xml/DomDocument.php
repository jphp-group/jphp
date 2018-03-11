<?php
namespace php\xml;
use Traversable;

/**
 * Class DomDocument
 * @package php\xml
 */
abstract class DomDocument extends DomElement
{
    /**
     * @return DomDocument
     */
    public function getDocumentElement() {}

    /**
     * @param string $id
     * @return DomElement
     */
    public function getElementById($id) {}

    /**
     * @return string
     */
    public function getInputEncoding() {}

    /**
     * @return string
     */
    public function getXmlEncoding() {}

    /**
     * @return string
     */
    public function getXmlVersion() {}

    /**
     * @return bool
     */
    public function getXmlStandalone() {}

    /**
     * @param bool $value
     */
    public function setXmlStandalone($value) {}

    /**
     * @return bool
     */
    public function getStrictErrorChecking() {}

    /**
     * @param bool $value
     */
    public function setStrictErrorChecking($value) {}

    /**
     * @return string
     */
    public function getDocumentURI() {}

    /**
     * @param string $value
     */
    public function setDocumentURI($value) {}

    /**
     * @param string $tagName
     * @param Traversable|array $model (optional)
     * @return DomElement
     */
    public function createElement($tagName, $model) {}

    /**
     * @param string $namespaceURI
     * @param string $qualifiedName
     * @return DomElement
     */
    public function createElementNS($namespaceURI, $qualifiedName) {}

    /**
     * @param string $name
     * @param string $value
     * @return DomNode
     */
    public function createProcessingInstruction($name, $value) {}

    /**
     * @param DomNode $importedNode
     * @param bool $deep
     * @return DomNode
     */
    public function importNode(DomNode $importedNode, $deep) {}

    /**
     * @param DomElement $importedNode
     * @param bool $deep
     * @return DomElement
     */
    public function importElement(DomElement $importedNode, $deep) {}

    /**
     * @param DomNode $source
     * @return DomNode
     */
    public function adoptNode(DomNode $source) {}

    /**
     * @param DomNode $node
     * @param string $namespaceURI
     * @param string $qualifiedName
     */
    public function renameNode(DomNode $node, $namespaceURI, $qualifiedName) {}

    /**
     *
     */
    public function normalizeDocument() {}
}