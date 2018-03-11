<?php
namespace php\gui;
use php\gui\text\UXFont;

/**
 * Class UXTooltip
 * @package php\gui
 * @packages gui, javafx
 */
class UXTooltip extends UXPopupWindow
{
    /**
     * @var string
     */
    public $text;

    /**
     * LEFT, RIGHT, CENTER, JUSTIFY
     * @var string
     */
    public $textAlignment;

    /**
     * CLIP, ELLIPSIS, WORD_ELLIPSIS, CENTER_ELLIPSIS, CENTER_WORD_ELLIPSIS, LEADING_ELLIPSIS, LEADING_WORD_ELLIPSIS
     * @var string
     */
    public $textOverrun;

    /**
     * @var UXFont
     */
    public $font;

    /**
     * @var UXNode
     */
    public $graphic;

    /**
     * @var double
     */
    public $graphicTextGap;

    /**
     * @var bool
     */
    public $activated;

    /**
     * @var bool
     */
    public $wrapText;

    /**
     * @param string $text
     * @param UXNode $graphic [optional]
     * @return UXTooltip
     */
    public static function of($text, UXNode $graphic)
    {
    }

    /**
     * @param UXNode $node
     * @param UXTooltip $tooltip
     */
    public static function install(UXNode $node, UXTooltip $tooltip) {}

    /**
     * @param UXNode $node
     * @param UXTooltip $tooltip
     */
    public static function uninstall(UXNode $node, UXTooltip $tooltip) {}
}