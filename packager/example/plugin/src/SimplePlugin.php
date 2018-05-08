<?php

use packager\cli\Console;
use packager\Event;

/**
 * Class SimplePlugin
 * @jppm-task-prefix simple
 *
 * @jppm-task myTask as my-task
 */
class SimplePlugin
{
    /**
     * @jppm-need-package
     * @jppm-description My Task from Simple plugin.
     * @param Event $event
     */
    public function myTask(Event $event)
    {
        Console::log("Hello JPPM, your package is '{0}'", $event->package()->getNameWithVersion());
    }
}