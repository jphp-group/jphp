<?php
namespace php\desktop;
use php\gui\UXImage;
use php\gui\UXScreen;

/**
 * Class Robot
 * @package php\desktop
 */
class Robot
{
    /**
     * Mouse X
     * @var int
     */
    public $x;

    /**
     * Mouse Y
     * @var int
     */
    public $y;

    /**
     * Mouse X, Y
     * @var int[]
     */
    public $position = [0, 0];

    /**
     * @param string $button
     */
    public function mouseClick($button = 'PRIMARY')
    {
    }

    /**
     * @param string $button PRIMARY, SECONDARY, MIDDLE
     */
    public function mouseDown($button = 'PRIMARY')
    {
    }

    /**
     * @param string $button PRIMARY, SECONDARY, MIDDLE
     */
    public function mouseUp($button = 'PRIMARY')
    {
    }

    /**
     * @param int $wheelAmt
     */
    public function mouseScroll($wheelAmt)
    {
    }

    /**
     * @param string $chars
     */
    public function type($chars)
    {
    }

    /**
     * @param string $keyCombination example Alt + Tab, Alt + Ctrl + Space, Ctrl + S
     */
    public function keyDown($keyCombination)
    {
    }

    /**
     * @param string $keyCombination example Alt + Tab, Alt + Ctrl + Space, Ctrl + S
     */
    public function keyUp($keyCombination)
    {
    }

    /**
     * @param string $keyCombination example Alt + Tab, Alt + Ctrl + Space, Ctrl + S
     */
    public function keyPress($keyCombination)
    {
    }

    /**
     * Make screen shot of screen (primary if null passed).
     * @param UXScreen|null $screen
     * @param array $bounds [x, y, width, height]
     * @return UXImage
     */
    public function screenshot(array $bounds = null, UXScreen $screen = null)
    {
    }
}