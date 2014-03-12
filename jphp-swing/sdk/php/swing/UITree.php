<?php
namespace php\swing;

use php\swing\tree\TreeModel;
use php\swing\tree\TreeNode;

/**
 * Class UITree
 * @package php\swing
 */
class UITree extends UIContainer {
    /**
     * @var TreeModel
     */
    public $model;

    /**
     * @var bool
     */
    public $rootVisible;

    /**
     * @var bool
     */
    public $editable;

    /**
     * @var int
     */
    public $visibleRowCount;

    /**
     * @var bool
     */
    public $dragEnabled;

    /**
     * @var bool
     */
    public $expandsSelectedPaths;

    /**
     * @var bool
     */
    public $invokesStopCellEditing;

    /**
     * @var bool
     */
    public $scrollsOnExpand;

    /**
     * @readonly
     * @var int
     */
    public $maxSelectionRow;

    /**
     * @readonly
     * @var int
     */
    public $minSelectionRow;

    /**
     * @readonly
     * @var int
     */
    public $leadSelectionRow;

    /**
     * @var int[]
     */
    public $selectionRows;

    /**
     * @readonly
     * @var int
     */
    public $rowCount;

    /**
     * @readonly
     * @var int
     */
    public $selectionCount;

    /**
     * @var int
     */
    public $rowHeight;

    /**
     * @var TreeNode
     */
    public $selectedNode;

    /**
     * @var TreeNode[]
     */
    public $selectedNodes;

    /**
     * @param callable $renderer
     *      (
     *          TreeNode $node,
     *          UILabel $template,
     *          bool $sel,
     *          bool $expand,
     *          bool $leaf,
     *          int $row,
     *          bool $hasFocus
     *       )
     */
    public function onCellRender(callable $renderer) { }

    /**
     * @param TreeNode $node
     */
    public function expandNode(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function collapseNode(TreeNode $node) { }
}
