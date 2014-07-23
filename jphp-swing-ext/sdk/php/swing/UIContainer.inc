<?php
namespace php\swing;

/**
 * Class UIContainer
 * @package php\swing
 */
abstract class UIContainer extends UIElement {
    /**
     * Add child component
     * @param UIElement $component
     * @param null|int $index
     * @param null|int $constraints
     */
    public function add(UIElement $component, $index = null, $constraints = null) { }

    /**
     * @param string $type - absolute, grid, flow, grid-bag, border, card
     */
    public function setLayout($type) { }

    /**
     * @param UIElement $component
     */
    public function remove(UIElement $component) { }

    /**
     * @param int $index
     * @throws
     */
    public function removeByIndex($index) { }

    /**
     * Remove all child components
     */
    public function removeAll() { }

    /**
     * @return int
     */
    public function getComponentCount() { }

    /**
     * @param int $index
     * @return UIElement
     * @throws
     */
    public function getComponent($index) { }

    /**
     * @return UIElement[]
     */
    public function getComponents() { return []; }

    /**
     * Find first component by group
     * @param string $group
     * @return UIElement|NULL
     */
    public function getComponentByGroup($group) { return 1; }

    /**
     * Find all components in group
     * @param string $group
     * @return UIElement[]
     */
    public function getComponentsByGroup($group) { }
}
