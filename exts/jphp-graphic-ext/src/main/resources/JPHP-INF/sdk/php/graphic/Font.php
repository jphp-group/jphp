<?php
namespace php\graphic;

/**
 * Class Font
 * @package php\graphic
 *
 * @packages graphic
 */
class Font
{
    /**
     * @readonly
     * @var string
     */
    public $name;

    /**
     * @readonly
     * @var string
     */
    public $fontName;

    /**
     * @readonly
     * @var string
     */
    public $family;

    /**
     * @readonly
     * @var int
     */
    public $size;

    /**
     * @readonly
     * @var bool
     */
    public $bold = false;

    /**
     * @readonly
     * @var bool
     */
    public $italic = false;

    /**
     * @readonly
     * @var bool
     */
    public $plain = false;

    /**
     * Font constructor.
     * @param string $name
     * @param int $size
     * @param bool $bold
     * @param bool $italic
     */
    public function __construct(string $name, int $size, bool $bold = false, bool $italic = false)
    {
    }

    /**
     * @param string $char
     * @return bool
     */
    public function canDisplay(string $char): bool
    {
    }

    /**
     * @return Font
     */
    public function asItalic(): Font
    {
    }

    /**
     * @return Font
     */
    public function asBold(): Font
    {
    }

    /**
     * @param float $size
     * @return Font
     */
    public function asWithSize(float $size): Font
    {
    }

    /**
     * @return Font[]
     */
    public static function allFonts(): array
    {
    }

    /**
     * Registers a created Font in this GraphicsEnvironment.
     * @param Font $font
     * @return bool
     */
    public static function register(Font $font): bool
    {
    }
}