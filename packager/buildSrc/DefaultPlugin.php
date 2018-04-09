<?php

use packager\cli\Console;
use packager\Event;
use packager\Package;
use packager\server\Server;
use packager\Vendor;
use php\io\Stream;
use php\lib\fs;
use php\lib\str;

/**
 * @jppm-task server
 * @jppm-task repo
 * @jppm-task install
 * @jppm-task add
 * @jppm-task remove
 * @jppm-task init
 * @jppm-task tasks
 */
class DefaultPlugin
{
    /**
     * @jppm-description show all tasks
     * @param Event $event
     */
    function tasks(Event $event)
    {
        global $app;

        Console::log("\nAvailable tasks:");

        Console::log("");

        foreach ($app->getCommands() as $command => $one) {
            ['handler' => $handler, 'description' => $desc] = $one;

            if ($desc) {
                Console::log(" -> $command - $desc");
            } else {
                Console::log(" -> $command");
            }
        }

        Console::log("");
    }

    /**
     * @jppm-description initialize new package
     *
     * @param Event $event
     */
    function init(Event $event)
    {
        $dir = fs::abs("./");

        if ($name = $event->args()[0]) {
            $dir = "$dir/$name";
        }

        if (fs::exists($dir . '/' . Package::FILENAME)) {
            Console::error("Failed to init, package '{0}' already exists", $dir . '/' . Package::FILENAME);
            exit(-1);
        }

        Console::log("Init new package in dir '$dir'':");

        if (!$name) {
            $name = fs::name(fs::parent($dir . "/foo"));
        }

        $version = "1.0.0";

        $name = Console::read("Enter name ($name):", $name);
        $version = Console::read("Enter version ($version):", $version);
        $description = Console::read("Enter description:", '');

        $addAppPlugin = Console::readYesNo("Add 'jphp app' plugin? (default = Yes)", 'yes');

        $data = [
            'name' => $name,
            'version' => $version,
        ];

        if ($description) {
            $data['description'] = $description;
        }

        if ($addAppPlugin) {
            $data['deps']['jphp-core'] = '*';
            $data['deps']['jphp-zend-ext'] = '*';

            $data['plugins'] = ['AppPlugin'];
            $data['app'] = [
                'bootstrap' => 'index.php',
                'encoding' => 'UTF-8',
                'metrics' => false,
                'trace' => false
            ];

            $data['sources'] = ['src'];

            Tasks::createDir("$dir/src");
            Tasks::createFile("$dir/src/index.php", "<?php \r\necho 'Hello World';\r\n");
        }

        $package = new Package($data, []);
        $event->packager()->writePackage($package, $dir);

        Console::log("Success, {0} has been created.", Package::FILENAME);
        Console::log("Done.");
    }

    /**
     * @jppm-description install dependencies or one.
     * @param Event $event
     */
    function install(Event $event)
    {
        global $app;

        $vendor = new Vendor("./vendor");

        if ($app->isFlag('clean')) {
            $vendor->clean();
            Console::log("The vendor dir has been cleared.");
        }

        $event->packager()->install($event->package(), $vendor, $app->isFlag('f', 'force'));
    }

    /**
     * @jppm-description add and install new dependencies to package.php.yml
     * @param Event $event
     */
    function add(Event $event)
    {
        $package = $event->package()->toArray();

        $modified = false;

        foreach ($event->args() as $arg) {
            [$dep, $version] = str::split($arg, '@');

            if ($package['deps'][$dep]) {
                Console::log("-> skip adding '{0}', already added.", $arg);
            } else {
                $pkg = $event->packager()->getRepo()->findPackage($dep, $version ?: '*');

                if ($pkg) {
                    $package['deps'][$dep] = $version ?: '*';
                    $modified = true;

                    Console::log("-> add '{0}' dependency, lock version = {1}", $arg, $pkg->getVersion($version));
                } else {
                    Console::error("Failed to add '{0}', is not found in repositories", $arg);
                    exit(-1);
                }
            }
        }

        if ($modified) {
            $event->packager()->writePackage(new Package($package, $event->package()->getInfo()), "./");

            Tasks::run('install');
        }
    }

    /**
     * @jppm-description remove and uninstall dependencies from package.php.yml
     * @param Event $event
     */
    function remove(Event $event)
    {
        $package = $event->package()->toArray();

        $modified = false;

        foreach ($event->args() as $arg) {
            [$dep, ] = str::split($arg, '@');

            if ($version = $package['deps'][$dep]) {
                Console::log("-> remove '{0}' dependency, version = {1}", $arg, $version ?: 'last');
                unset($package['deps'][$dep]);
                $modified = true;
            } else {
                Console::error("Failed to remove '{0}' dependency, is not found in {1}", $arg, Package::FILENAME);
                exit(-1);
            }
        }

        if ($modified) {
            $event->packager()->writePackage(new Package($package, $event->package()->getInfo()), "./");
            Tasks::run('install');
        }
    }

    /**
     * @jppm-description run jppm server on 6333 port
     * @param Event $event
     */
    function server(Event $event)
    {
        $server = new Server($event->packager()->getRepo());
        $server->run();
    }

    /**
     * @jppm-description manage local repository.
     * @param Event $event
     */
    function repo(Event $event)
    {
        $args = $event->args();

        switch ($args[0]) {
            case "index":
                $event->packager()->getRepo()->indexAll($args[1] ?: null);
                break;

            case "index:one":
                if (!$args[1]) {
                    Console::error("jppm repo index <module> [<destDir>], module is not passed.");
                    exit(-1);
                }

                $event->packager()->getRepo()->index($args[1],$args[2] ?: null);
                break;

            default:
                Console::error("Command 'repo {0}' not found. Try to run 'help' via 'jppm tasks'.", $args[0]);
                exit(-1);
        }
    }
}