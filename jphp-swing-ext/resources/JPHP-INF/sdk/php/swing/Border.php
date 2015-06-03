<?php
namespace php\swing;

/**
 * Class Border
 * @package php\swing
 */
class Border {

    /**
     * @return bool
     */
    public function isOpaque() { return false; }

    /**
     * @param int $top
     * @param int $left
     * @param int $bottom
     * @param int $right
     * @return Border
     */
    public static function createEmpty($top, $left, $bottom, $right) { return new Border(); }

    /**
     * @param string $type - RAISED or LOWERED
     * @param Color|array|int $highlightColor
     * @param Color|array|int $shadowColor
     * @return Border
     */
    public static function createBevel($type, $highlightColor, $shadowColor) { return new Border(); }

    /**
     * @param string $type - RAISED or LOWERED
     * @param Color|array|int $highlightColor
     * @param Color|array|int $shadowColor
     * @return Border
     */
    public static function createSoftBevel($type, $highlightColor, $shadowColor) { return new Border(); }


    /**
     * @param string $type - RAISED or LOWERED
     * @param Color|array|int $highlightColor
     * @param Color|array|int $shadowColor
     * @return Border
     */
    public static function createEtchedBevel($type, $highlightColor, $shadowColor) { return new Border(); }

    /**
     * @param string $title
     * @param Border $border
     * @param Font $titleFont
     * @param Color|array|int $titleColor
     * @return Border
     */
    public static function createTitled($title, Border $border = null, Font $titleFont = null, $titleColor = null) {
        return new Border();
    }

    /**
     * @param Color|array|int $color
     * @param int $size
     * @param bool $rounded
     * @return Border
     */
    public static function createLine($color, $size = 1, $rounded = false) { return new Border(); }

    /**
     * @param Color|array|int $color
     * @param int $thickness
     * @param int $length
     * @param int $spacing
     * @param bool $rounded
     * @return Border
     */
    public static function createDashed($color, $thickness = 1, $length = 2, $spacing = 1, $rounded = false) { return new Border(); }
}
