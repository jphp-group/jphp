<?php
namespace ext\javafx\layout;

use ext\javafx\layout\UXPane;
use ext\javafx\UXNode;

/**
 * Class UXAnchorPane
 * @package ext\javafx\layout
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
}