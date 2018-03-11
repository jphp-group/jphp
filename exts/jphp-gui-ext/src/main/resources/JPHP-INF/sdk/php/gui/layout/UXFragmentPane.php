<?php
namespace php\gui\layout;

use php\gui\UXForm;
use php\gui\UXNode;
use php\gui\UXParent;

/**
 * @package php\gui\layout
 * @packages gui, javafx
 */
class UXFragmentPane extends UXVBox
{
    /**
     * @readonly
     * @var UXParent
     */
    public $layout;

    /**
     * @param UXForm|null $form
     */
    public function applyFragment(UXForm $form)
    {
    }
}