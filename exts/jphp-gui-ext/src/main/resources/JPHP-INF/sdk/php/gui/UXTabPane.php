<?php
namespace php\gui;
use php\gui\layout\UXPane;

/**
 * Class UXTabPane
 * @package php\gui
 * @packages gui, javafx
 */
class UXTabPane extends UXControl
{
    /**
     * @var UXList
     */
    public $tabs;

    /**
     * @var UXTab
     */
    public $selectedTab;

    /**
     * @var int
     */
    public $selectedIndex = -1;

    /**
     * @var string SELECTED_TAB, ALL_TABS, UNAVAILABLE
     */
    public $tabClosingPolicy = 'SELECTED_TAB';

    /**
     * @var string
     */
    public $side = 'TOP';

    /**
     * @return UXPane
     */
    public static function createDefaultDnDPane()
    {
    }

    /**
     * @param UXTab $tab
     */
    public function selectTab(UXTab $tab) { }

    /**
     * ...
     */
    public function selectFirstTab() {}

    /**
     * ...
     */
    public function selectNextTab() {}

    /**
     * ...
     */
    public function selectLastTab() {}

    /**
     * ...
     */
    public function selectPreviousTab() {}

    /**
     * ...
     */
    public function clearSelection() {}
}