<?php
namespace php\gui;

/**
 * Class UXDialog
 * @package php\gui
 * @packages gui, javafx
 */
class UXDialog
{
    /**
     * @param $text
     * @param string $type
     * @param UXWindow $owner
     * @return null|string
     */
    public static function show($text, $type = 'INFORMATION', UXWindow $owner = null)
    {
    }

    /**
     * @param $text
     * @param string $type
     * @param UXWindow $owner
     */
    public static function showAndWait($text, $type = 'INFORMATION', UXWindow $owner = null)
    {
    }

    /**
     * @param $text
     * @param UXNode $content
     * @param $expanded
     * @param $type
     */
    public static function showExpanded($text, UXNode $content, $expanded, $type = 'INFORMATION')
    {
    }

    /**
     * @param $text
     * @param UXWindow $owner
     * @return bool
     */
    public static function confirm($text, UXWindow $owner = null)
    {
    }

    /**
     * @param $text
     * @param string $default
     * @param UXWindow $owner
     * @return null|string
     */
    public static function input($text, $default = '', UXWindow $owner = null)
    {
    }
}