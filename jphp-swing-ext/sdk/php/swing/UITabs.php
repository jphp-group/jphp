<?php
namespace php\swing;

/**
 * Class UITabs
 * @package php\swing
 */
class UITabs extends UIContainer {
    /**
     * @var int
     */
    public $selectedIndex;

    /**
     * @var UIElement
     */
    public $selectedComponent;

    /**
     * @var string - left, right, top, bottom
     */
    public $tabPlacement;

    /**
     * @readonly
     * @var int
     */
    public $tabCount;

    /**
     * @param string $title
     * @param UIElement $component
     * @param Image $icon
     */
    public function addTab($title, UIElement $component, Image $icon = null) { }

    /**
     * @param int $index
     * @return string
     */
    public function getTitleAt($index) { }

    /**
     * @param int $index
     * @param string $value
     */
    public function setTitleAt($index, $value) { }

    /**
     * @param int $index
     * @return string
     */
    public function getToolTipTextAt($index) { }

    /**
     * @param int $index
     * @param string $value
     */
    public function setToolTipTextAt($index, $value) { }

    /**
     * @param int $index
     * @return Image
     */
    public function getTabIconAt($index) { }

    /**
     * @param int $index
     * @param Image $image
     */
    public function setTabIconAt($index, Image $image) { }

    /**
     * @param int $index
     * @return UIElement
     */
    public function getTabComponentAt($index) { }

    /**
     * @param int $index
     * @param UIElement $component
     */
    public function setTabComponentAt($index, UIElement $component) { }

    /**
     * @param int $index
     */
    public function removeTabAt($index) { }

    /**
     * removes all tabs
     */
    public function removeAll() { }

    /**
     * @param int $x
     * @param int $y
     * @return int
     */
    public function indexAtPosition($x, $y) { }

    /**
     * @param int $index
     * @return bool
     */
    public function isEnabledAt($index) { }

    /**
     * @param int $index
     * @param bool $enabled
     */
    public function setEnabledAt($index, $enabled) { }
}
