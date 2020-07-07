<?php

namespace packager\cli;

use DefaultPlugin;
use system\DFFIConsole;
use TemplatePlugin;
use function flow;
use function is_array;
use const JPHP_VERSION;
use packager\Annotations;
use packager\Event;
use packager\JavaExec;
use packager\Package;
use packager\Packager;
use packager\Repository;
use packager\repository\GithubReleasesRepository;
use packager\repository\GithubRepository;
use packager\repository\GitRepository;
use packager\repository\LocalDirRepository;
use packager\repository\ServerRepository;
use packager\server\Server;
use packager\Vendor;
use php\io\File;
use php\io\IOException;
use php\io\Stream;
use php\lang\Invoker;
use php\lang\Module;
use php\lang\System;
use php\lang\Thread;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use php\time\Timer;
use semver\SemVersion;
use Tasks;
use text\TextWord;
use function var_dump;

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
        DFFIConsole::enableColors();
        
        try {
            Repository::registerExternalRepositoryClass(GitRepository::class);
            Repository::registerExternalRepositoryClass(LocalDirRepository::class);

            Repository::registerExternalRepositoryClass(GithubRepository::class);
            Repository::registerExternalRepositoryClass(GithubReleasesRepository::class);
            Repository::registerExternalRepositoryClass(ServerRepository::class);

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

            $osName = System::osName();
            $os = null;

            if ($this->isFlag('without-os')) {
                // nop.
            } else {
                if ($_ENV['JPPM_OS_BUILD']) {
                    $os = $_ENV['JPPM_OS_BUILD'];
                } else {
                    if ($this->isFlag('win')) {
                        $os = 'win';
                    } else if ($this->isFlag('mac') || $this->isFlag('darwin')) {
                        $os = 'mac';
                    } else if ($this->isFlag('linux')) {
                        $os = 'linux';
                    } else if (str::posIgnoreCase($osName, 'win') > -1) {
                        $os = 'win';
                    } else if (str::posIgnoreCase($osName, 'mac') > -1) {
                        $os = 'mac';
                    } else {
                        $os = 'linux';
                    }
                }
            }

            if ($os) {
                Console::log("-> $os");
                Package::setOS($os);
            }

            $command = $args[1];

            if ($this->isFlag('debug')) {
                $this->debug = true;
                Console::log("args = " . var_export($args, true));
            }

            $this->loadPlugin(DefaultPlugin::class);

            if ($this->getPackage()) {
                $this->loadPlugins();

                foreach ($this->getPackage()->getRepos() as $repo) {
                    $this->packager->getRepo()->addExternalRepoByString($repo);
                }

                $tasks = $this->packager->loadTasks($this->getPackage());

                foreach ($tasks as $name => $task) {
                    if ($this->commands[$name]) {
                        Console::error("Task '{0}' already exists. Duplicate names in the 'tasks' section.", $name);
                        exit(-1);
                    }

                    if (!is_array($task)) {
                        Console::error("Task '{0}' is invalid format, check your 'tasks' section.", $name);
                        exit(-1);
                    }

                    $this->addCommand($name, function () use ($task) {
                        foreach ((array) $task['depends-on'] as $one) {
                            $one = flow(str::split($one, ' '))->map([str::class, 'trim'])->toArray();

                            $flags = flow($one)
                                ->find(function ($x) { return $x[0] === '-'; })
                                ->map(function ($x) { return str::sub($x, 1); })
                                ->toArray();

                            $args = flow($one)
                                ->find(function ($x) { return $x[0] !== '-'; })
                                ->toArray();

                            Tasks::run($one[0], $args, $flags);
                        }
                    }, $task['description']);
                }

                $this->loadBuildScript();
            }

            if ($this->getPackage()) {
                if ($errors = $this->packager->checkPackage($this->getPackage())) {
                    foreach ($errors as $error) {
                        Console::error($error);
                    }

                    exit(-1);
                }
            }

            foreach (str::split($command, '+') as $item) {
                $this->invokeTask($item, flow($args)->skip(2)->toArray(), ...flow($this->flags)->keys());
            }
        } finally {
            Timer::shutdownAll();
        }
    }

    /**
     * @return bool
     */
    public function isDebug(): bool
    {
        return $this->debug;
    }

    function invokeTask(string $task, array $args, ...$flags)
    {
        if ($this->taskUpDate[$task . '#' . str::join($flags, ',')]) {
            Console::log("-> $task (up-to-date)");
            return;
        }

        $flags = arr::combine($flags, $flags);

        $this->taskUpDate[$task . '#' . str::join($flags, ',')] = true;

        switch ($task) {
            case "version":
                Console::log('JPPM Information:');
                Console::log("-> version: {0}", $this->packager->getVersion());
                Console::log("--> jphp: {0}", JPHP_VERSION);
                Console::log("--> java: {0} ({1})", System::getProperty("java.version"), System::getProperty("os.arch"));
                Console::log("-> home: '{0}'", System::getProperty("jppm.home"));
                Console::log("--> java home: '{0}'", $_ENV['JAVA_HOME']);

                break;

            default:
                $command = $this->commands[$task];

                if ($handler = $command['handler']) {
                    foreach ($command['dependsOn'] as $one) {
                        $this->invokeTask($one, $args, ...$flags);
                    }

                    Console::log("-> {0} {1}", $task, ($flags ? '-' : '') . flow($flags)->keys()->toString(' -'));

                    $handler($args, $flags);
                    break;
                } else {
                    $means = [];

                    foreach ($this->commands as $name => $info) {
                        $name = new TextWord($name);

                        if ($name->jaroWinklerDistance($task) > 0.9) {
                            $means[] = "$name";
                        }
                    }

                    Console::error("Task '{0}' not found. Try to run 'jppm tasks' to show all available tasks.", $task);


                    if ($means) {
                        Console::log("\n   The most similar command is:\n");
                        foreach ($means as $mean) {
                            Console::log("     - {0}", $mean);
                        }
                    }

                    exit(-1);
                }
        }
    }

    protected function loadPlugin($plugin)
    {
        if (class_exists("{$plugin}Plugin")) {
            $plugin = "{$plugin}Plugin";
        }

        if (class_exists($plugin)) {
            $class = new \ReflectionClass($plugin);
            $prefix = Annotations::getOfClass('jppm-task-prefix', $class, "");
            $tasks = Annotations::getOfClass('jppm-task', $class, []);

            $pluginObject = null;

            foreach ($tasks as $task) {
                [$task, $taskName] = str::split($task, ' as ');
                $task = str::trim($task);
                $taskName = str::trim($taskName ?? $task);

                if (method_exists($plugin, $task)) {
                    $context = null;
                    $handler = new \ReflectionMethod($plugin, $task);

                    if (!$handler->isStatic()) {
                        if (!$pluginObject) {
                            $pluginObject = new $plugin(new Event($this->packager, $this->getPackage(), []));
                        }

                        $context = $pluginObject;
                    }

                    $description = Annotations::getOfMethod('jppm-description', $handler, "$plugin::$task");
                    $dependsOn = Annotations::getOfMethod('jppm-depends-on', $handler, []);
                    $dependencyOf = Annotations::getOfMethod('jppm-dependency-of', $handler, []);


                    $needPackage = Annotations::getOfMethod('jppm-need-package', $handler);
                    $needDependsOn = Annotations::getOfMethod('jppm-need-depends-on', $handler);

                    $taskName = $prefix ? "$prefix:$taskName" : $taskName;

                    $this->addCommand($taskName, function ($args, $flags = []) use ($handler, $context, $taskName, $needPackage, $needDependsOn) {
                        if ($needPackage && !$this->getPackage()) {
                            Console::error("'{0}' task needs a package.php.yml", $taskName);
                            exit(-1);
                        }

                        if ($needDependsOn && !$this->commands[$taskName]['dependsOn']) {
                            Console::error("'{0}' task cannot be executed, there are no suitable dependency tasks.", $taskName);
                            exit(-1);
                        }

                        $flags = flow($this->flags, $flags)->toMap();

                        $handler->invokeArgs($context, [new Event($this->packager, $this->getPackage(), $args, $flags)]);
                    }, $description, $dependsOn);

                    foreach ($dependencyOf as $dependency) {
                        if ($this->commands[$dependency]) {
                            $this->commands[$dependency]['dependsOn'][] = $taskName;
                        }
                    }
                } else {
                    Console::warn("Cannot add task '{0}', method '{1}' not found in '{2}'", $taskName, $task, $plugin);
                }
            }
        } else {
            Console::error("Incorrect plugin '{0}', class not found.", $plugin);
        }
    }

    protected function loadBuildScript()
    {
        if ($this->getPackage()) {
            if (fs::isFile("./package.build.php")) {
                $module = new Module("./package.build.php");
                $module->call();

                foreach ($module->getFunctions() as $function) {
                    if (str::startsWith($function->getName(), "task_")) {
                        $task = Annotations::get('jppm-task', $function->getDocComment(), str::sub($function->getName(), 5));

                        $this->addCommand(
                            $task,
                            function ($args, $flags = []) use ($function) {
                                $function->invoke(new Event($this->packager, $this->getPackage(), $args, $flags));
                            },
                            Annotations::get('jppm-description', $function->getDocComment(), 'from package.build.php')
                        );
                    }
                }
            }
        }
    }

    protected function loadPlugins()
    {
        if ($this->getPackage()) {
            $vendor = new Vendor($this->getPackage()->getConfigVendorPath());
            $paths = $vendor->fetchPaths();

            if (is_array($paths['classPaths']['plugin'])) {
                foreach ($paths['classPaths']['plugin'] as $classPath) {
                    System::addClassPath($vendor->getDir() . $classPath);
                }
            }

            if (is_array($paths['plugins'])) {
                flow($paths['plugins'])->each([$this, 'loadPlugin']);
            }

            $plugins = $this->getPackage()->getAny('plugins', []);

            foreach ((array) $plugins as $key => $plugin) {
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
        static $pkg;

        if ($pkg === null) {
            try {
                $dir = fs::abs("./");
                $repo = $this->packager->getRepo();
                $pkg = $repo->readPackage("$dir/" . Package::FILENAME);

                foreach ($this->packager->getProfiles() as $profile) {
                    $profilePkg = $repo
                        ->readPackage("$dir/" . str::format(Package::FILENAME_S, $profile));
                    
                    $pkg->mergePackage($profilePkg);
                }

                return $pkg;
            } catch (IOException $e) {
                return null;
            }
        } else {
            return $pkg;
        }
    }

    function addCommand(string $name, callable $handle, string $description = '', array $dependsOn = [])
    {
        $this->commands[$name] = ['handler' => $handle, 'description' => $description, 'dependsOn' => $dependsOn];
    }
}