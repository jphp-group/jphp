<?php

use packager\cli\Console;
use packager\Event;
use php\io\Stream;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use php\util\Regex;

/**
 * @jppm-task init
 */
class TemplatePlugin
{

    /**
     * @jppm-description init project with template
     * @param Event $event
     */
    function init(Event $event)
    {
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

                        foreach ($template["variables"] as $variable => $data) {
                            $variables[$variable] = $this->getVariable($data);
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
    }

    /**
     * @param $data
     * @return string|null
     */
    public function getVariable($data)
    {
        if (is_string($data)) {
            return Console::read(" -> " . $data . ":");
        } else if (is_array($data)) {
            if (isset($data["default"])) {
                $res = Console::read(" -> " . $data["message"] . ":", $data["default"]);
            } else {
                $res = Console::read(" -> " . $data["message"]. ":");
            }

            if (isset($data["regex"])) {
                if (Regex::match($data["regex"], $res))
                    return $res;
                else {
                    if ($data["regex-error-message"]) {
                        Console::log("  -> " . $data["regex-error-message"]);
                    } else {
                        Console::log("  -> argument mismatch with regular expression `{$data['regex']}`");
                    }

                    return $this->getVariable($data);
                }
            } else {
                return $res;
            }
        } else return null;
    }

    /**
     * @param string $templateSource
     * @param array $variables
     */
    private function copyTemplate(string $templateSource, array $variables)
    {
        fs::scan($templateSource . "/", function (string $file) use ($templateSource, $variables) {
            if (fs::ext($file) == "template") {
                $this->copyTemplateFile($templateSource, $file, "/" . fs::nameNoExt($file), $variables);
            } else {
                $this->copyTemplateFile($templateSource, $file);
            }
        });
    }

    /**
     * @param string $templateSource
     * @param string $file
     * @param string|null $newName
     * @param array $variables
     * @throws \php\io\IOException
     */
    private function copyTemplateFile(string $templateSource, string $file, string $newName = null, array $variables = [])
    {
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