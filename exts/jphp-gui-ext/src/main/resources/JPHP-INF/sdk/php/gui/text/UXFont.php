<?php
namespace php\gui\text;

use php\io\Stream;

/**
 * Class UXFont
 * @package php\gui\text
 * @packages gui, javafx
 */
class UXFont
{
    /**
     * @var string
     */
    public $name;

    /**
     * @var string
     */
    public $family;

    /**
     * @var int
     */
    public $size;

    /**
     * @var string
     */
    public $style;

    /**
     * @var bool
     */
    public $bold = false;

    /**
     * @var bool
     */
    public $italic = false;

    /**
     * @readonly
     * @var float
     */
    public $lineHeight;

    /**
     * @param double $size
     * @param string $family (optional)
     */
    public function __construct($size, $family)
    {
    }

    /**
     * @param $name
     * @return UXFont
     */
    public function withName($name)
    {
    }

    /**
     * @param $size
     * @return UXFont
     */
    public function withSize($size)
    {
    }

    /**
     * @param $name
     * @param $size
     */
    public function withNameAndSize($name, $size)
    {
    }

    /**
     * @return UXFont
     */
    public function withBold()
    {
    }

    /**
     * @return UXFont
     */
    public function withThin()
    {
    }

    /**
     * @return UXFont
     */
    public function withItalic()
    {
    }

    /**
     * @return UXFont
     */
    public function withoutItalic()
    {
    }

    /**
     * @return UXFont
     */
    public function withRegular()
    {
    }

    /**
     * @param string $text
     * @return float
     */
    public function calculateTextWidth($text)
    {
    }

    /**
     * Generate CSS style of font.
     *
     * @return string
     */
    public function generateStyle(): string
    {
    }

    /**
     * @param string $family
     * @param double $size
     *
     * @param string $fontWeight THIN, BOLD, NORMAL, MEDIUM, EXTRA_BOLD, BLACK
     * @param bool $italic
     * @return UXFont
     */
    public static function of($family, $size, $fontWeight = 'THIN', $italic = false)
    {
    }

    /**
     * @param Stream $stream
     * @param double $size
     *
     * @return UXFont
     */
    public static function load(Stream $stream, $size)
    {
    }

    /**
     * @return UXFont
     */
    public static function getDefault()
    {
    }

    /**
     * @param string $family (optional)
     *
     * @return string[]
     */
    public static function getFontNames($family)
    {
    }

    /**
     * @return string[]
     */
    public static function getFamilies()
    {
    }
}