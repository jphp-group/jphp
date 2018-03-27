<?php
namespace packager\cli;
use packager\Java;
use packager\Package;
use packager\Packager;
use packager\Repository;
use packager\server\Server;
use packager\Vendor;
use php\io\File;
use php\io\IOException;
use php\io\Stream;
use php\lang\Thread;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;

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

        if ($this->getPackage()) {
            $plugins = $this->packager->loadPlugins($this->getPackage());
            foreach ($plugins as $plugin) {
                $plugin->beforeConsole($this);
            }
        }

        switch ($command) {
            case "version":
                Console::log('JPHP Packager Welcome');
                Console::log("- Version {0}", Packager::VERSION);
                Console::log("- JPHP Version {0}", JPHP_VERSION);
                break;

            default:
                $command = str::replace($command,"-", "_");

                if (method_exists($this, "handle$command")) {
                    $method = [$this, "handle$command"];
                    $method(flow($args)->skip(2)->toArray());
                    break;
                } else {
                    if ($handler = $this->commands[$command]) {
                        $handler(flow($args)->skip(2)->toArray());
                        break;
                    } else {
                        $stderr->write("[Packager]: Command '$command' not found. Try to run 'help' via 'jppm tasks'.");
                        exit(-1);
                    }
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

    function getPackage(): ?Package
    {
        try {
            $dir = fs::abs("./");
            return $this->packager->getRepo()->readPackage("$dir/" . Package::FILENAME);
        } catch (IOException $e) {
            return null;
        }
    }

    function addCommand(string $name, callable $handle)
    {
        $this->commands[$name] = $handle;
    }

    function handleRepo(array $args)
    {
        $stderr = Stream::of("php://stderr");

        switch ($args[0]) {
            case "index":
                $this->packager->getRepo()->indexAll($args[1] ?: null);
                break;

            default:
                $stderr->write("[Packager]: Command 'repo $args[0]' not found. Try to run 'help' via 'jppm tasks'.");
                exit(-1);
        }
    }

    function handleServer(array $args)
    {
        $server = new Server($this->packager->getRepo());
        $server->run();
    }

    function handleTasks(array $args)
    {
        Console::log("Available tasks:");

        Console::log("- init // init package");
        Console::log("- install // install deps");
        Console::log("- tasks // show all tasks");
        Console::log("- server // run jppm server on 6333 port");

        Console::log("");

        foreach ($this->commands as $command => $handler) {
            Console::log("- $command");
        }
    }

    function handleInstall(array $args)
    {
        $vendor = new Vendor("./vendor");

        if ($this->isFlag('clean')) {
            $vendor->clean();
            Console::log("The vendor dir has been cleared.");
        }

        $this->packager->install($this->getPackage(), $vendor, $this->isFlag('f', 'force'));
    }

    function handleInit(array $args)
    {
        $dir = fs::abs("./");

        if (fs::exists($dir . '/' . Package::FILENAME)) {
            $this->fail("Failed to init, package '" . ($dir . Package::FILENAME) . "' already exists!");
        }

        Console::log("Init new package in dir '$dir'':");

        $name = fs::name(fs::parent($dir . "/foo"));
        $version = "1.0.0";

        $name = Console::read("question name ($name):", $name);
        $version = Console::read("question version ($version):", $version);
        $description = Console::read("question description:", '');

        $data = [
            'name' => $name,
            'version' => $version,
        ];

        if ($description) {
            $data['description'] = $description;
        }

        $package = new Package($data);
        $this->packager->writePackage($package, $dir);

        Console::log("Success, {0} has been created.", Package::FILENAME);

        Console::log("Done.");
    }
}