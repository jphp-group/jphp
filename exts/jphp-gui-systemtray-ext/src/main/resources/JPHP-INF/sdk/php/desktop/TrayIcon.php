<?php
namespace php\desktop;

use php\gui\event\UXEvent;
use php\gui\UXImage;

/**
 * Class TrayIcon
 * @package php\desktop
 *
 * events:
 *      click, mouseMove, mouseEnter, mouseExit
 */
class TrayIcon
{
    /**
     * @var UXImage
     */
    public $image;

    /**
     * @var bool
     */
    public $imageAutoSize = false;

    /**
     * @var string
     */
    public $tooltip;

    /**
     * TrayIcon constructor.
     * @param UXImage $image
     */
    public function __construct(UXImage $image)
    {
        $this->image = $image;
    }
    
    /**
     * Displays a popup message near the tray icon.  The message will
     * disappear after a time or if the user clicks on it.  Clicking
     * on the message may trigger an ActionEvent.
     *
     * @param string $title
     * @param string $text
     * @param string $type NONE, INFO, ERROR, WARNING
     */
    public function displayMessage($title, $text, $type = 'NONE')
    {
    }

    /**
     * @param string $event
     * @param callable $handler
     * @param string $group
     */
    public function on($event, callable $handler, $group = 'general')
    {
    }

    /**
     * @param string $event
     * @param string $group (optional)
     */
    public function off($event, $group)
    {
    }

    /**
     * @param string $event
     * @param UXEvent $e (optional)
     */
    public function trigger($event, UXEvent $e)
    {
    }
}