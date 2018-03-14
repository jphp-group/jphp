<?php
namespace packager\cli;
use packager\Package;
use packager\Packager;
use php\io\Stream;
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

        switch ($command) {
            case "-v":
            case "--version":
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
                    $stderr->write("[Packager]: Command '$command' not found. Try to run 'help' via 'jppm help'.");
                    exit(-1);
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

    function getPackage(): Package
    {
        $dir = fs::abs("./");
        return $this->packager->getRepo()->readPackage("$dir/" . Package::FILENAME);
    }

    function handleHelp(array $args)
    {
        Console::log("Available commands:");
        Console::log("- init // create new package.php.yml");
        Console::log("- add [package-name] [-g] // add dep to your package and install it");
        Console::log("- remove [package-name] [-g] // remove dep from your package and uninstall it");
        Console::log("- info [package-name] // show information about package");
    }

    function handleAdd(array $args)
    {
        $packageName = $args[0];
    }

    function handleInstall(array $args)
    {
        Console::log("Installing vendors ...");

        $this->packager->installVendors($this->getPackage(), "./vendor");

        Console::log("Done.");
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