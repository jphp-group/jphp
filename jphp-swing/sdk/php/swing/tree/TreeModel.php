<?php
namespace php\swing\tree;

/**
 * Class TreeModel
 * @package php\swing\tree
 */
class TreeModel {
    /**
     * @var TreeNode
     */
    public $root;

    /**
     * @param TreeNode $root
     * @param bool $askAllowsChildren
     */
    public function __construct(TreeNode $root, $askAllowsChildren = false) { }

    /**
     * @param TreeNode $node
     */
    public function nodeChanged(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function nodeStructureChanged(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function removeNodeFromParent(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function reload(TreeNode $node = null) { }
}
