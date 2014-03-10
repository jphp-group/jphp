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
     */
    public function add(UIElement $component, $index = null) { }

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
     * Find first component by group
     * @param string $group
     * @return UIElement|NULL
     */
    public function getComponentByGroup($group) { }

    /**
     * Find all components in group
     * @param string $group
     * @return UIElement[]
     */
    public function getComponentsByGroup($group) { }
}
