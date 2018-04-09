<?php

namespace packager\cli;

use DefaultPlugin;
use packager\Annotations;
use packager\Event;
use packager\JavaExec;
use packager\Package;
use packager\Packager;
use packager\Repository;
use packager\server\Server;
use packager\Vendor;
use php\io\File;
use php\io\IOException;
use php\io\Stream;
use php\lang\Invoker;
use php\lang\System;
use php\lang\Thread;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use Tasks;

/**
 * Class ConsoleApp
 * @package packager\cli
 */
class ConsoleApp
{
    private $debug = false;
    private $flags = [];

    /**
     * @var callable[]
     */
    private $commands = [];

    /**
     * @var Packager
     */
    private $packager;

    private $taskUpDate = [];

    function main(array $args)
    {
        $stderr = Stream::of("php://stderr");

        $this->packager = new Packager();

        $args = flow($args)->find(function ($arg) {
            if (str::startsWith($arg, "--")) {
                $this->flags[str::sub($arg, 2)] = true;
                return false;
            }

            if (str::startsWith($arg, "-")) {
                $this->flags[str::sub($arg, 1)] = true;
                return false;
            }

            return true;
        })->toArray();

        $command = $args[1];

        if ($this->isFlag('debug')) {
            Console::log("args = " . var_export($args, true));
        }

        $this->loadPlugin(DefaultPlugin::class);

        if ($this->getPackage()) {
            $this->loadPlugins();
            $scripts = $this->packager->loadTasks($this->getPackage());

            foreach ($scripts as $bin => $handler) {
                $invoker = Invoker::of($handler);

                $description = Annotations::get(
                    'jppm-description',
                    $invoker->getDescription(),
                    "script " . (is_string($handler) ? $handler : '')
                );

                $dependsOn = Annotations::get('jppm-depends-on', $invoker->getDescription(), []);

                $this->addCommand($bin, function ($args) use ($invoker, $handler) {
                    $invoker->call(new Event($this->packager, $this->getPackage(), $args));
                }, $description, $dependsOn);
            }
        }

        $this->invokeTask($command, flow($args)->skip(2)->toArray());
    }

    function invokeTask(string $task, array $args)
    {
        if ($this->taskUpDate[$task]) {
            Console::log("\r[$task] Skip (up-to-date)");
            return;
        }

        $this->taskUpDate[$task] = true;

        switch ($task) {
            case "version":
                Console::log('JPHP Packager Welcome');
                Console::log("- Version {0}", Packager::VERSION);
                Console::log("- JPHP Version {0}", JPHP_VERSION);
                break;

            default:
                $command = $this->commands[$task];

                if ($handler = $command['handler']) {
                    foreach ($command['dependsOn'] as $one) {
                        $this->invokeTask($one, $args);
                    }

                    Console::log("-> {0}", $task);

                    $handler($args);
                    break;
                } else {
                    Console::error("Task '{0}' not found. Try to run 'help' via 'jppm tasks'.", $task);
                    exit(-1);
                }
        }
    }

    protected function loadPlugin($plugin)
    {
        if (class_exists($plugin)) {
            $class = new \ReflectionClass($plugin);
            $prefix = Annotations::getOfClass('jppm-task-prefix', $class, "");
            $tasks = Annotations::getOfClass('jppm-task', $class, []);

            foreach ($tasks as $task) {
                if (method_exists($plugin, $task)) {
                    $handler = new \ReflectionMethod($plugin, $task);

                    $description = Annotations::getOfMethod('jppm-description', $handler, "$plugin::$task");
                    $dependsOn = Annotations::getOfMethod('jppm-depends-on', $handler, []);

                    $this->addCommand($prefix ? "$prefix:$task" : $task, function ($args) use ($handler) {
                        $handler->invokeArgs(null, [new Event($this->packager, $this->getPackage(), $args)]);
                    }, $description, $dependsOn);
                } else {
                    Console::warn("Cannot add task '{0}', method not found in '{1}'", $task, $plugin);
                }
            }
        } else {
            Console::error("Incorrect plugin '{0}', class not found.", $plugin);
        }
    }

    protected function loadPlugins()
    {
        if ($this->getPackage()) {
            $plugins = $this->getPackage()->getAny('plugins', []);

            foreach ($plugins as $key => $plugin) {
                $this->loadPlugin($plugin);
            }
        }
    }

    function isFlag(...$names): bool
    {
        foreach ($names as $name) {
            if ($this->flags[$name]) return true;
        }

        return false;
    }

    function fail(string $massage, int $status = -1)
    {
        $stderr = Stream::of("php://stderr");
        $stderr->write($massage);

        exit($status);
    }

    /**
     * @return callable[]
     */
    public function getCommands(): array
    {
        return $this->commands;
    }

    function getPackage(): ?Package
    {
        try {
            $dir = fs::abs("./");
            return $this->packager->getRepo()->readPackage("$dir/" . Package::FILENAME);
        } catch (IOException $e) {
            return null;
        }
    }

    function addCommand(string $name, callable $handle, string $description = '', array $dependsOn = [])
    {
        $this->commands[$name] = ['handler' => $handle, 'description' => $description, 'dependsOn' => $dependsOn];
    }
}