<?php
namespace php\swing;

/**
 * Class UIInternalForm
 * @package php\swing
 */
class UIInternalForm extends UIContainer {

    /**
     * @var string
     */
    public $title;

    /**
     * @var bool
     */
    public $selected;

    /**
     * @var bool
     */
    public $resizable;

    /**
     * @param UIDesktopPanel $panel
     */
    public function setLayeredPanel(UIDesktopPanel $panel) { }

    /**
     * @param UIContainer $content
     */
    public function setContent(UIContainer $content) { }
}
