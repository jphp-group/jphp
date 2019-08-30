<?php

use packager\cli\Console;
use packager\Event;

/**
 * jppt - tool to easily creating custom projects with templates (jppm packages)
 *
 * @jppm-task tasks
 * @jppm-task init
 */
class TemplatePlugin
{
    /**
     * @jppm-description show all tasks
     * @param Event $event
     */
    function tasks(Event $event)
    {
        $defaultPlugin = new DefaultPlugin();
        $defaultPlugin->tasks($event);
    }

    /**
     * @jppm-description init project with template
     * @param Event $event
     */
    function init(Event $event)
    {

    }
}