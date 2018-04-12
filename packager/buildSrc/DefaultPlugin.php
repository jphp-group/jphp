<?php

use compress\GzipOutputStream;
use compress\TarArchive;
use compress\TarArchiveEntry;
use packager\cli\Console;
use packager\Event;
use packager\Package;
use packager\PackageDependencyTree;
use packager\Repository;
use packager\server\Server;
use packager\Vendor;
use php\format\JsonProcessor;
use php\io\File;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;

/**
 * @jppm-task server
 * @jppm-task repo
 * @jppm-task install
 * @jppm-task add
 * @jppm-task remove
 * @jppm-task deps
 * @jppm-task init
 * @jppm-task tasks
 * @jppm-task publish
 * @jppm-task unpublish
 * @jppm-task pack
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

        $data['repos'] = ['jphp', 'central'];

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
            Tasks::createFile("$dir/src/index.php", "<?php \r\necho \"Hello World\\n\";\r\n");
        }

        $package = new Package($data, []);
        $event->packager()->writePackage($package, $dir);

        Console::log("Success, {0} has been created.", Package::FILENAME);
        Console::log("Done.");
    }

    /**
     * @jppm-description publish to local repository.
     * @param Event $event
     */
    function publish(Event $event)
    {
        $pkg = $event->package();
        if ($pkg) {
            if ($oldPkg = $event->packager()->getRepo()->getPackage($pkg->getName(), $pkg->getVersion())) {
                if (!Console::readYesNo("Package {$pkg->getName()}@{$pkg->getVersion()} already published, re-publish it?")) {
                    Console::log("... canceled.");
                    return;
                }
            }

            Console::log(" -> publishing, wait ...");

            $event->packager()->getRepo()->installFromDir("./");

            Console::log("");
            Console::log("  Use '{0}: \"{1}\"' to add this package as dependency.", $pkg->getName(), $pkg->getVersion());
            Console::log("");

            Console::log("Package {0}@{1} was published successfully.", $pkg->getName(), $pkg->getVersion());
        }
    }

    /**
     * @jppm-description un-publish from local repository.
     * @param Event $event
     */
    function unpublish(Event $event)
    {
        $pkg = $event->package();

        if ($pkg) {
            if ($oldPkg = $event->packager()->getRepo()->getPackage($pkg->getName(), $pkg->getVersion())) {
                Console::log(" -> un-publishing, wait ...");

                $event->packager()->getRepo()->delete($oldPkg);
                Console::log("Package {0}@{1} was un-published successfully.", $pkg->getName(), $pkg->getVersion());
            } else {
                Console::log("Package {0}@{1} is not published!", $pkg->getName(), $pkg->getVersion());
            }
        }
    }

    /**
     * @jppm-description install dependencies or one.
     * @param Event $event
     */
    function install(Event $event)
    {
        if ($event->package()) {
            global $app;

            $vendor = new Vendor("./vendor");

            if ($app->isFlag('clean')) {
                $vendor->clean();
                Console::log("The vendor dir has been cleared.");
            }

            $event->packager()->install($event->package(), $vendor, $app->isFlag('f', 'force'));
        }
    }

    /**
     * @jppm-description add and install new dependencies to package.php.yml
     * @param Event $event
     */
    function add(Event $event)
    {
        global $app;

        $repo = $event->packager()->getRepo();

        if ($app->isFlag('g', 'global')) {
            Console::log("-> global mode is enabled.");

            foreach ($event->args() as $arg) {
                [$dep, $version] = str::split($arg, '@');

                if ($version === '') {
                    $version = '*';
                }

                $pkg = $repo->findPackage($dep, $version);
                if ($pkg) {
                    if ($pkg->getInfo()['new']) {
                        Console::log("-> add '{0}@{1}'.", $pkg->getName(), $pkg->getVersion($version));
                    } else {
                        Console::log("-> skip adding '{0}@{1}', already added.", $pkg->getName(), $pkg->getVersion($version));
                    }
                } else {
                    Console::error("Failed to add '{0}', is not found in repositories", $arg);
                    exit(-1);
                }
            }

            return;
        }

        if ($event->package()) {
            $package = $event->package()->toArray();

            $modified = false;

            foreach ($event->args() as $arg) {
                [$dep, $version] = str::split($arg, '@');

                if ($package['deps'][$dep]) {
                    Console::log("-> skip adding '{0}', already added.", $arg);
                } else {
                    $pkg = $repo->findPackage($dep, $version ?: '*');

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
    }

    /**
     * @jppm-description remove and uninstall dependencies from package.php.yml
     * @param Event $event
     */
    function remove(Event $event)
    {
        global $app;

        $repo = $event->packager()->getRepo();

        if ($app->isFlag('g', 'global')) {
            Console::log("-> global mode is enabled.");

            foreach ($event->args() as $arg) {
                [$dep, $version] = str::split($arg, '@');

                if ($version) {
                    if (!$repo->getPackage($dep, $version)) {
                        Console::error("Failed to remove '{0}@{1}', is not added.", $dep, $version);
                        exit(-1);
                    }

                    if ($repo->delete(new Package(['name' => $dep, 'version' => $version]))) {
                        Console::log("-> remove '{0}@{1}'", $dep, $version);
                    } else {
                        Console::error("Failed to remove '{0}@{1}', cannot delete directory.", $dep, $version);
                        exit(-1);
                    }
                } else {
                    $result = $repo->deleteByName($dep, function (Package $pkg, bool $success) {
                        if ($success) {
                            Console::log("-> remove '{0}'", $pkg->getNameWithVersion());
                        } else {
                            Console::error("Failed to remove '{0}', cannot delete directory.", $pkg->getNameWithVersion());
                            exit(-1);
                        }
                    });

                    if (!$result) {
                        Console::error("Failed to remove '{0}', is not added.", $arg);
                        exit(-1);
                    }
                }
            }

            return;
        }

        if ($event->package()) {
            $package = $event->package()->toArray();

            $modified = false;

            foreach ($event->args() as $arg) {
                [$dep,] = str::split($arg, '@');

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
    }

    /**
     * @jppm-description show all dependencies of project.
     * @param Event $event
     */
    function deps(Event $event)
    {
        $tree = $event->packager()->fetchDependencyTree($event->package(), '');
        $devTree = $event->packager()->fetchDependencyTree($event->package(), 'dev');

        foreach (['' => $tree, 'dev' => $devTree] as $name => $one) {
            if ($name) {
                Console::log("\n'{0}' dependencies:\n", $name);
            }

            $one->eachDep(function (Package $pkg, PackageDependencyTree $tree, int $depth = 0) {
                $prefix = str::repeat('-', $depth);

                Console::log("{0}- {1}@{2}", $prefix, $pkg->getName(), $pkg->getVersion());
            });
        }
    }

    /**
     * @jppm-description create a tarball from a package
     * @param Event $event
     */
    function pack(Event $event)
    {
        if ($pkg = $event->package()) {
            Tasks::run('install');

            $file = File::createTemp("{$pkg->getName()}-{$pkg->getVersion()}", ".tar.gz");
            Tasks::createFile($file);

            $localFile = "./{$pkg->getName()}-{$pkg->getVersion()}.tar.gz";
            $localVerFile = "./{$pkg->getName()}-{$pkg->getVersion()}.json";

            Tasks::deleteFile($localFile);
            Tasks::deleteFile($localVerFile);

            $path = "./";
            $info = Repository::calcPackageInfo($path);

            $arch = new TarArchive(new GzipOutputStream($file));
            $arch->open();

            foreach (arr::sort(fs::scan($path)) as $one) {
                if (fs::isFile($one)) {
                    $name = fs::relativize($one, $path);
                    if ($name === "README.md") {
                        continue;
                    }

                    $arch->addFile($one, $name);
                }
            }

            $arch->close();

            fs::formatAs($localVerFile, $info, 'json', JsonProcessor::SERIALIZE_PRETTY_PRINT);
            fs::copy($file, $localFile);
            Tasks::deleteFile($file, true);

            Console::log(fs::abs($localFile));
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