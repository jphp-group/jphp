<?php
namespace php\swing;

/**
 * Class UIManager
 */
final class UIManager {

    private function __construct(){}

    /**
     * Set look and feel globally
     * @param $name - name of theme
     */
    public static function setLookAndFeel($name){}

    /**
     * Get name of OS native look and feel
     * @return string
     */
    public static function getSystemLookAndFeel(){}
}