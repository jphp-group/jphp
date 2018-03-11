<?php
namespace php\gui\layout;

use php\gui\layout\UXPane;
use php\gui\UXNode;

/**
 * Class UXAnchorPane
 * @package php\gui\layout
 * @packages gui, javafx
 */
class UXAnchorPane extends UXPane
{
    /**
     * @param UXNode $node
     * @return double
     */
    static function getLeftAnchor(UXNode $node) {}

    /**
     * @param UXNode $node
     * @return double
     */
    static function getRightAnchor(UXNode $node) {}

    /**
     * @param UXNode $node
     * @return double
     */
    static function getTopAnchor(UXNode $node) {}

    /**
     * @param UXNode $node
     * @return double
     */
    static function getBottomAnchor(UXNode $node) {}

    /**
     * @param UXNode $node
     * @param double $value
     */
    static function setLeftAnchor(UXNode $node, $value) {}

    /**
     * @param UXNode $node
     * @param double $value
     */
    static function setRightAnchor(UXNode $node, $value) {}

    /**
     * @param UXNode $node
     * @param double $value
     */
    static function setTopAnchor(UXNode $node, $value) {}

    /**
     * @param UXNode $node
     * @param double $value
     */
    static function setBottomAnchor(UXNode $node, $value) {}

    /**
     * @param UXNode $node
     * @param double $value
     */
    static function setAnchor(UXNode $node, $value) {}
}