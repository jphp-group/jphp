<?php
namespace php\gdx\graphics;

/**
 * Class DisplayMode
 * @package php\gdx\graphics
 */
class DisplayMode {
    private function __construct() { }

    /**
     * @return int
     */
    public function getWidth() { }

    /**
     * @return int
     */
    public function getHeight() { }

    /**
     * @return int
     */
    public function getBitsPerPixel() { }

    /**
     * @return int
     */
    public function getRefreshRate() { }

    /**
     * @return string
     */
    public function __toString() { return ''; }
} 