<?php

use packager\cli\Console;
use packager\Event;
use php\io\Stream;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;

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
        if (arr::count($event->args()) >= 1) {
            $templateName = $event->arg(0);
            $version = "*";
            $package = $event->packager()->getRepo()->findPackage($templateName, $version);

            if ($package) {
                $version = $package->getVersion("*");

                if ($package->getType() == "template") {
                    $directory = $event->packager()->getRepo()->getDirectory() . "/$templateName/$version";

                    if (fs::isDir($directory)) {
                        $template = $package->getAny("template");
                        $variables = [];

                        if ($template["variables"]) {
                            Console::log("-> setting variables ...");

                            foreach ($template["variables"] as $variable => $message) {
                                $variables[$variable] = Console::read(" -> " . $message . ":");
                            }
                        }

                        foreach ($template["sources"] as $source) {
                            Console::log("-> copy sources from `$source`");
                            $this->copyTemplate(fs::abs($directory . "/$source/"), $variables);
                        }

                        if ($template["tasks"]) {
                            foreach ($template["tasks"] as $task) {
                                Console::log("-> executing task `$task`");
                                Tasks::runExternal("./", $task);
                            }
                        }
                    } else {
                        Console::error("Package version `$version` not found!");
                    }
                } else {
                    Console::error("Package `$templateName` not template!");
                    exit(1);
                }
            } else {
                Console::error("Package `$templateName` not found!");
                exit(1);
            }
        } else {
            Console::error("Usage: jppt init <package>");
            exit(1);
        }
    }

    /**
     * @param string $templateSource
     * @param array $variables
     */
    private function copyTemplate(string $templateSource, array $variables) {
        fs::scan($templateSource . "/", function (string $file) use ($templateSource, $variables) {
            if (fs::ext($file) == "template") {
                $this->copyTemplateFile($templateSource, $file, "/" . fs::nameNoExt($file), $variables);
            } else {
                $this->copyTemplateFile($templateSource, $file);
            }
        });
    }

    private function copyTemplateFile(string $templateSource, string $file, string $newName = null, array $variables = []) {
        $name = fs::abs("./") . "/" . fs::relativize($file, $templateSource);

        if (fs::isDir($file)) {
            fs::makeDir("$name");
            return;
        }

        fs::ensureParent($name);
        fs::copy($file, $name);

        $content = Stream::getContents($name);

        foreach ($variables as $variable => $value) {
            $content = str::replace($content, "{{" . $variable . "}}", $value);
        }

        if ($newName) {
            Stream::putContents(fs::parent($name) . $newName, $content);
            fs::delete($name);
        } else {
            Stream::putContents($name, $content);
        }
    }
}