<?php
namespace php\gui;

use php\gui\event\UXEvent;

/**
 * Class UXTrayNotification
 * @package php\gui
 * @packages gui, javafx
 *
 * events - click, show, hide
 */
class UXTrayNotification
{
    /**
     * @var string
     */
    public $title;

    /**
     * @var string
     */
    public $message;

    /**
     * @var string INFORMATION, NOTICE, WARNING, SUCCESS, ERROR, CUSTOM
     */
    public $notificationType = 'NOTICE';

    /**
     * @var string FADE, SLIDE, POPUP
     */
    public $animationType = 'FADE';

    /**
     * @var string BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT, TOP_RIGHT
     */
    public $location = 'BOTTOM_RIGHT';

    /**
     * @var UXImage
     */
    public $image;

    /**
     * @deprecated
     * @var UXImage
     */
    public $trayIcon;

    /**
     * @var int
     */
    public $horGap = 5;

    /**
     * @var int
     */
    public $verGap = 5;

    /**
     * @param string $title
     * @param string $message
     * @param string $type
     */
    public function __construct($title = '', $message = '', $type = 'NOTICE')
    {
    }

    /**
     * @param int $delay in millis
     */
    public function show($delay = 5000)
    {
    }

    /**
     * ...
     */
    public function showAndWait()
    {
    }

    public function hide()
    {
    }


    /**
     * @param string $event finish, etc.
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