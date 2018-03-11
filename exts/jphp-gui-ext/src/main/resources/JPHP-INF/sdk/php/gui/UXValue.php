<?php
namespace php\gui;
use php\lang\Invoker;

/**
 * Class UXValue
 * @package php\gui
 * @packages gui, javafx
 */
class UXValue
{
    /**
     * @return mixed
     */
    public function getValue()
    {
    }

    /**
     * @param callable $handle
     * @return Invoker
     */
    public function addListener(callable $handle)
    {
    }

    /**
     * Add listener which call only once!
     * @param callable $handle
     * @return Invoker
     */
    public function addOnceListener(callable $handle)
    {
    }

    /**
     * @param Invoker $invoker
     * @return bool
     */
    public function removeListener(Invoker $invoker)
    {
    }
}