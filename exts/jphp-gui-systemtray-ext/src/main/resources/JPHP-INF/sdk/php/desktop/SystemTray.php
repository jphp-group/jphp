<?php
namespace php\desktop;


/**
 * Class SystemTray
 * @package php\desktop
 */
class SystemTray
{
    private function __construct()
    {
    }

    /**
     * @return bool
     */
    public static function isSupported()
    {
    }

    /**
     * @param TrayIcon $icon
     */
    public static function add(TrayIcon $icon)
    {
    }

    /**
     * @param TrayIcon $icon
     */
    public static function remove(TrayIcon $icon)
    {
    }

    /**
     * @return TrayIcon[]
     */
    public static function getTrayIcons()
    {
    }

    /**
     * @return double[]
     */
    public static function getTrayIconSize()
    {
    }
}