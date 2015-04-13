<?php
namespace php\gdx;

use php\gdx\graphics\DisplayMode;

/**
 * Class ApplicationConfiguration
 * @package php\gdx
 */
class LwjglApplicationConfiguration {
    /**
     * @var bool
     */
    public $useGL30;

    /**
     * @var int
     */
    public $depth = 16;

    /**
     * @var int
     */
    public $samples = 0;

    /**
     * @var int
     */
    public $width = 640;

    /**
     * @var int
     */
    public $height = 480;

    /**
     * @var int
     */
    public $x = -1;

    /**
     * @var
     */
    public $y = -1;

    /**
     * @var bool
     */
    public $fullscreen = false;

    /**
     * @var bool
     */
    public $vSyncEnabled = true;

    /**
     * @var string
     */
    public $title;

    /**
     * @var bool
     */
    public $forceExit = true;

    /**
     * @var bool
     */
    public $resizable = true;

    /**
     * @var int
     */
    public $audioDeviceSimultaneousSources = 16;

    /**
     * @var int
     */
    public $audioDeviceBufferSize = 512;

    /**
     * @var int
     */
    public $audioDeviceBufferCount = 9;

    /**
     * @var int
     */
    public $foregroundFPS = 60;

    /**
     * @var int
     */
    public $backgroundFPS = 60;

    /**
     * @var bool
     */
    public $allowSoftwareMode = false;

    /**
     * @var string
     */
    public $preferencesDirectory = ".prefs/";

    /**
     * @param DisplayMode $displayMode
     */
    public function setFromDisplayMode(DisplayMode $displayMode) { }

    /**
     * @return DisplayMode
     */
    public static function getDesktopDisplayMode() { }

    /**
     * @return DisplayMode[]
     */
    public static function getDisplayModes() { }

    /**
     * Adds a window icon. Icons are tried in the order added, the first one that works is used. Typically three icons should be
     * provided: 128x128 (for Mac), 32x32 (for Windows and Linux), and 16x16 (for Windows).
     *
     * @param string $path
     * @param string $type - Classpath, Internal, External, Absolute, Local
     */
    public static function addIcon($path, $type) { }
} 