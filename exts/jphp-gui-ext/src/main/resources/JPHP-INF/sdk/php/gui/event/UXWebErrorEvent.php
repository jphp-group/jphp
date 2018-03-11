<?php
namespace php\gui\event;

use php\gui\UXNode;
use php\lang\JavaException;

/**
 * Class UXWebErrorEvent
 * @package php\gui\event
 * @packages gui, javafx
 */
class UXWebErrorEvent extends UXEvent
{
    /**
     * @readonly
     * @var mixed
     */
    public $message;

    /**
     * @readonly
     * @var JavaException
     */
    public $exception;
}