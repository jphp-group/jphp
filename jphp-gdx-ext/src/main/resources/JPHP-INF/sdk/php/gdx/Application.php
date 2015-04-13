<?php
namespace php\gdx;

/**
 * Class Application
 * @package php\gdx
 */
abstract class Application {
    const LOG_NONE = 0;
    const LOG_DEBUG = 3;
    const LOG_INFO = 2;
    const LOG_ERROR = 1;

    /**
     * @return Graphics
     */
    public function getGraphics() { }

    /**
     * @return Files
     */
    public function getFiles() { }

    /**
     * @return Input
     */
    public function getInput() { }

    /**
     * @return Audio
     */
    public function getAudio() { }

    /**
     * @param string $tag
     * @param string $message
     */
    public function log($tag, $message) { }

    /**
     * @param string $tag
     * @param string $message
     */
    public function error($tag, $message) { }

    /**
     * @param string $tag
     * @param string $message
     */
    public function debug($tag, $message) { }

    /**
     * @param int $level
     */
    public function setLogLevel($level) { }

    /**
     * @return int
     */
    public function getLogLevel() { }

    /**
     * @return string - Android, Desktop, HeadlessDesktop, Applet, WebGL, iOS
     */
    public function getType() { }

    /**
     * @return int the Android API level on Android, the major OS version on iOS (5, 6, 7, ..), or 0 on the desktop.
     */
    public function getVersion() { }

    /**
     * @return int  the Java heap memory use in bytes
     */
    public function getJavaHeap() { }

    /**
     * @return int the Native heap memory use in bytes
     */
    public function getNativeHeap() { }

    /**
     * Schedule an exit from the application. On android, this will cause a call to pause() and dispose() some time in the future,
     * it will not immediately finish your application.
     */
    public function halt() { }

    /**
     * @return Clipboard
     */
    public function getClipboard() { }
}