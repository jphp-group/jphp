<?php
namespace php\swing;

use php\swing\tree\TreeModel;
use php\swing\tree\TreeNode;

/**
 * Class UITree
 * @package php\swing
 *
 * @events
 *      - expanded  (UITree $tree, TreeNode $node)
 *      - willExpand  (UITree $tree, TreeNode $node)
 *      - collapsed  (UITree $tree, TreeNode $node)
 *      - willCollapse  (UITree $tree, TreeNode $node)
 *      - selected  (UITree $tree, TreeNode $node = null, TreeNode $oldNode = null, bool $isAdded)
 */
class UITree extends UIContainer {
    /**
     * @var TreeModel
     */
    public $model;

    /**
     * @var TreeNode
     */
    public $root;

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
     * @readonly
     * @var TreeNode
     */
    public $editingNode;

    /**
     * @var string - ALWAYS, HIDDEN, AUTO
     */
    public $horScrollPolicy;

    /**
     * @var string - ALWAYS, HIDDEN, AUTO
     */
    public $verScrollPolicy;

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
    public function addSelectionNode(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function removeSelectionNode(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function expandNode(TreeNode $node) { }

    /**
     * @param int $row
     */
    public function expandRow($row) { }

    /**
     * @param TreeNode $node
     */
    public function collapseNode(TreeNode $node) { }

    /**
     * @param int $row
     */
    public function collapseRow($row) { }

    /**
     * @param TreeNode $node
     */
    public function expandNodeAll(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function collapseNodeAll(TreeNode $node) { }

    /**
     * @param int $row
     * @return bool
     */
    public function isExpandedRow($row) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isExpandedNode(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isNodeSelected(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isNodeEditable(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function isVisible(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @return bool
     */
    public function hasBeenExpanded(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function fireTreeExpanded(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function fireTreeCollapsed(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @throws
     */
    public function fireTreeWillExpand(TreeNode $node) { }

    /**
     * @param TreeNode $node
     * @throws
     */
    public function fireTreeWillCollapse(TreeNode $node) { }

    /**
     * @param TreeNode $node
     */
    public function makeVisible(TreeNode $node) { }

    /**
     * ...
     */
    public function cancelEditing() { }

    /**
     * ...
     */
    public function clearSelection() { }
}
