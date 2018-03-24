<?php
namespace packager\cli;
use packager\Java;
use packager\Package;
use packager\Packager;
use packager\Repository;
use packager\Vendor;
use php\io\File;
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

        $this->packager->install($this->getPackage(), new Vendor("./vendor"));

        Console::log("Done.");
    }

    function handleRun(array $args)
    {
        $launcher = $this->getPackage()->getAny('launcher');

        $vendor = new Vendor("./vendor");
        $classPaths = flow(fs::parseAs("{$vendor->getDir()}/classPaths.json", 'json')[''])
            ->map(function ($cp) use ($vendor) { return "{$vendor->getDir()}/$cp"; })
            ->toArray();

        if ($jars = $this->getPackage()->getJars()) {
            foreach ($jars as $jar) {
                $classPaths[] = fs::abs("./jars/$jar");
            }
        }

        if ($sources = $this->getPackage()->getSources()) {
            foreach ($sources as $src) {
                $classPaths[] = fs::abs("./$src");
            }
        }

        $args = [
            '-cp', str::join($classPaths, File::PATH_SEPARATOR),
        ];

        if ($launcher['trace']) {
            $args[] = '-Djphp.trace=true';
        }

        $args[] = '-Dfile.encoding=' . ($launcher['encoding'] ?: 'UTF-8');

        if ($launcher['bootstrap']) {
            $args[] = '-Dbootstrap.file=res://' . $launcher['bootstrap'];
        }

        if (is_array($launcher['jvmArgs'])) {
            $args = flow($args, $launcher['jvmArgs'])->toArray();
        }

        $args[] = $launcher['mainClass'] ?: 'php.runtime.launcher.Launcher';

        if (is_array($launcher['args'])) {
            $args = flow($args, $launcher['args'])->toArray();
        }

        $process = Java::exec($args);

        $process = $process->start();

        $input = $process->getInput();
        $err = $process->getError();

        (new Thread(function () use ($input) {
            $stdout = Stream::of('php://stdout');

            while ($ch = $input->read(1)) {
                $stdout->write($ch);
            }
        }))->start();

        (new Thread(function () use ($err, $process) {
            $stdout = Stream::of('php://stderr');

            while ($ch = $err->read(1)) {
                $stdout->write($ch);
            }
        }))->start();
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