<?php
namespace php\swing;

use php\io\IOException;
use php\io\Stream;

class Font {
    const PLAIN  = 0;
    const BOLD   = 1;
    const ITALIC = 2;

    /**
     * Family name of font
     * @readonly
     * @var string
     */
    public $family;

    /**
     * @readonly
     * @var string
     */
    public $fontName;

    /**
     * @readonly
     * @var string
     */
    public $name;

    /**
     * PostScript name of font
     * @readonly
     * @var string
     */
    public $psName;

    /**
     * @readonly
     * @var int
     */
    public $size;

    /**
     * @readonly
     * @var int
     */
    public $size2D;

    /**
     * @readonly
     * @var int
     */
    public $style;

    /**
     * @readonly
     * @var double
     */
    public $italicAngle;

    /**
     * @readonly
     * @var string[]
     */
    public $attributes;

    /**
     * @readonly
     * @var int
     */
    public $numGlyphs;

    /**
     * @param string $name
     * @param int $style - PLAIN, BOLD, ITALIC
     * @param int $size
     */
    public function __construct($name, $style, $size){ }

    /**
     * @return bool
     */
    public function isBold() { }

    /**
     * @return bool
     */
    public function isItalic() { }

    /**
     * @return bool
     */
    public function isPlain() { }

    /**
     * @return bool
     */
    public function isTransformed() { }

    /**
     * @param string $symbol - one char
     * @return int
     */
    public function getBaselineFor($symbol) { }

    /**
     * @param string $symbol - one char
     * @return bool
     */
    public function canDisplay($symbol) { }

    /**
     * Indicates whether or not this Font can display a specified String.
     * @param string $string
     * @return int - an offset into $string that points to the first character in $string that this
     *          Font cannot display; or -1 if this Font can display all characters in $string.
     */
    public function canDisplayUpTo($string) { }

    /**
     * Decode font by using a specified string
     * @param string $str
     * @return Font
     */
    public static function decode($str) { }

    /**
     * Create new font by using Stream or File
     * @param string|Stream $source
     * @param bool $trueType
     * @throws IOException
     * @throws \Exception
     * @return Font
     */
    public static function create($source, $trueType = true){ }

    /**
     * Get font by name
     * @param string $name
     * @return Font|null - return null if not exists
     */
    public static function get($name){ }
}
