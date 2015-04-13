<?php
namespace php\gdx;
use php\gdx\graphics\DisplayMode;

/**
 * Class Graphics
 * @package php\gdx
 */
class Graphics {
    /**
     * @return bool
     */
    public function isGL30Available() { }

    /**
     * @return int
     */
    public function getWidth() { }

    /**
     * @return int
     */
    public function getHeight() { }

    /**
     * @return double
     */
    public function getDeltaTime() { }

    /**
     * @return double
     */
    public function getRawDeltaTime() { }

    /**
     * @return int
     */
    public function getFramesPerSecond() { }

    /**
     * @return float
     */
    public function getDensity() { }

    /**
     * @return bool
     */
    public function supportsDisplayModeChange() { }

    /**
     * @return DisplayMode[] the supported fullscreen DisplayMode(s)
     */
    public function getDisplayModes() { }

    /**
     * @return DisplayMode
     */
    public function getDesktopDisplayMode() { }

    /**
     * @param DisplayMode|int $widthOrDisplayMode
     * @param int $height (optional)
     * @param int $fullscreen (optional)
     */
    public function setDisplayMode($widthOrDisplayMode, $height, $fullscreen) { }

    /**
     * @param string $title
     */
    public function setTitle($title) { }

    /**
     * @param bool $vsync
     */
    public function setVSync($vsync) { }

    /**
     * @param string $extension
     * @return bool
     */
    public function supportsExtension($extension) { }

    /**
     * @param bool $value
     */
    public function setContinuousRendering($value) { }

    /**
     * @return bool
     */
    public function isContinuousRendering() { }

    /**
     * Requests a new frame to be rendered if the rendering mode is non-continuous.
     * This method can be called from any thread.
     */
    public function requestRendering() { }

    /**
     * @return bool
     */
    public function isFullscreen() { }
}