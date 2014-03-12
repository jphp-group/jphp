<?php
namespace php\swing\tree;

/**
 * Class TreeNode
 * @package php\swing\tree
 */
class TreeNode {
    /**
     * @readonly
     * @var int
     */
    public $depth;

    /**
     * @readonly
     * @var int
     */
    public $level;

    /**
     * @var bool
     */
    public $allowsChildren;

    /**
     * @var TreeNode
     */
    public $parent;

    /**
     * @var mixed
     */
    public $userData;

    /**
     * @param mixed $object
     * @param bool $allowsChildren
     */
    public function __construct($object = null, $allowsChildren = true) { }

    /**
     * @return bool
     */
    public function isRoot() { }

    /**
     * @return bool
     */
    public function isLeaf() { }

    /**
     * @return TreeNode
     */
    public function getRoot() { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isNodeChild(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isNodeAncestor(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isNodeDescendant(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isNodeRelated(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isNodeSibling(TreeNode $node) { }

    /**
     * @return TreeNode|null
     */
    public function getNextNode() { }

    /**
     * @return TreeNode|null
     */
    public function getNextLeaf() { }

    /**
     * @return TreeNode|null
     */
    public function getNextSibling() { }

    /**
     * @return TreeNode|null
     */
    public function getPreviousNode() { }

    /**
     * @return TreeNode|null
     */
    public function getPreviousLeaf() { }

    /**
     * @return TreeNode|null
     */
    public function getPreviousSibling() { }

    /**
     * @return TreeNode|null
     */
    public function getFirstNode() { }

    /**
     * @return TreeNode|null
     */
    public function getFirstLeaf() { }

    /**
     * @return TreeNode|null
     */
    public function getLastNode() { }

    /**
     * @return TreeNode|null
     */
    public function getLastLeaf() { }

    /**
     * @param TreeNode $node
     */
    public function add(TreeNode $node) { }

    /**
     * @param int $childIndex
     * @param TreeNode $node
     */
    public function insert($childIndex, TreeNode $node) { }

    /**
     * @param TreeNode $child
     * @param TreeNode $node
     * @throws
     */
    public function insertAfter(TreeNode $child, TreeNode $node) { }

    /**
     * @param TreeNode $child
     * @param TreeNode $node
     * @throws
     */
    public function insertBefore(TreeNode $child, TreeNode $node) { }

    /**
     * @param TreeNode $child
     */
    public function remove(TreeNode $child) { }

    /**
     * @param int $childIndex
     */
    public function removeByIndex($childIndex) { }

    public function removeAllChildren() { }

    public function removeFromParent() { }

    /**
     * @param TreeNode $node
     * @return int
     */
    public function getIndex(TreeNode $node) { }

    /**
     * @return TreeNode
     */
    public function duplicate() { }
}
