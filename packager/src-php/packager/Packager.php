<?php

namespace packager;

use packager\cli\Console;
use php\format\YamlProcessor;
use php\io\IOException;
use php\io\Stream;
use php\lang\System;
use php\lang\ThreadPool;
use php\lib\fs;
use php\lib\str;
use php\time\Time;
use php\time\Timer;
use Tasks;

/**
 * Class Packager
 * @package packager
 */
class Packager
{
    private $version;

    /**
     * @var Repository
     */
    private $repo;

    /**
     * @var Ignore
     */
    private $ignore;

    /**
     * @var PackageLoader
     */
    private $packageLoader;

    /**
     * @var PackageLock
     */
    private $packageLock;

    /**
     * Packager constructor.
     */
    public function __construct()
    {
        global $argv;

        $home = $argv[0];
        if (fs::isFile($home)) {
            $home = fs::parent($home);
        }

        System::setProperty("jppm.home", $home);

        $this->version = fs::parse("$home/package.php.yml")['version'];

        $dir = System::getProperty('user.home') . '/.jppm';

        if (!fs::isDir($dir)) {
            fs::makeDir($dir);
        }

        $this->repo = new Repository("$dir/repo");
        $this->ignore = Ignore::ofDir("./");

        $this->packageLoader = new PackageLoader();
        $this->packageLock = new PackageLock();
    }

    /**
     * @return mixed
     */
    public function getVersion()
    {
        return $this->version;
    }

    /**
     * @return Ignore
     */
    public function getIgnore(): Ignore
    {
        return $this->ignore;
    }

    /**
     * @param Package $package
     * @return array
     */
    public function loadTasks(Package $package): array
    {
        $buildSrcDir = "./buildSrc/";

        spl_autoload_register(function ($className) use ($buildSrcDir) {
            $fileName = str::replace($className, "\\", "/");

            $fileName = "$buildSrcDir/$fileName.php";

            if (fs::isFile($fileName)) {
                require $fileName;
            }
        });

        return $package->getTasks();
    }

    public function install(Package $source, Vendor $vendor, bool $forceUpdate = false)
    {
        fs::makeDir($vendor->getDir());

        $thPool = null; //ThreadPool::create(4, 8);

        $tree = $this->fetchDependencyTree($source, '', null, $thPool);
        $devTree = $this->fetchDependencyTree($source, 'dev', null, $thPool);

        if ($thPool instanceof ThreadPool) {
            while ($thPool->getActiveCount() > 0) {
                Timer::sleep('0.1s');
            }

            $thPool->shutdown();
        }

        $this->packageLock->load($vendor->getDir() . "/../");
        $this->packageLoader->clean();

        $usedPackages = [];

        foreach (['' => $tree, 'dev' => $devTree] as $scope => $one) {
            /** @var PackageDependencyTree $one */
            $one->eachDep(function (Package $pkg, PackageDependencyTree $tree, int $depth = 0) use ($forceUpdate, $vendor, $scope, &$usedPackages) {
                $usedPackages[$pkg->getName()] = true;
                $prefix = str::repeat('-', $depth);

                if ($forceUpdate || !$vendor->alreadyInstalled($pkg, $this->packageLock)) {
                    Console::log("{$prefix}-> install {0}@{1}", $pkg->getName(), $pkg->getRealVersion());

                    $this->repo->copyTo($pkg, $vendor->getDir());
                }

                $this->packageLock->addPackage($pkg);

                $this->packageLoader->registerPackage($pkg, $vendor, $scope);
            });

            foreach ($one->getInvalidDeps() as $name => $version) {
                Console::warn("-> failed to install {0}@{1}, cannot find in repositories.", $name, $version);
            }
        }

        foreach (fs::scan($vendor->getDir(), ['excludeFiles' => true], 1) as $pkgName) {
            if (!$usedPackages[fs::name($pkgName)]) {
                Console::log("-> remove unnecessary package '{0}' from vendor directory.", fs::name($pkgName));
                Tasks::deleteFile($pkgName);
            }
        }

        $this->packageLoader->save($vendor);
        $this->packageLock->save($vendor->getDir() . "/../");
    }

    /**
     * @param Package $package
     * @param string $scope '' or 'dev'
     * @param PackageDependencyTree $parent
     * @param null|ThreadPool $threadPool
     * @return PackageDependencyTree
     */
    public function fetchDependencyTree(Package $package, string $scope = '', ?PackageDependencyTree $parent = null, ?ThreadPool $threadPool = null): PackageDependencyTree
    {
        $result = new PackageDependencyTree($package, $parent);

        $deps = ['' => $package->getDeps(), 'dev' => $package->getDevDeps()];

        foreach ($deps[$scope] as $dep => $version) {
            if ($parent && $parent->findByName($dep)) {
                continue;
            }

            $handler = function () use ($dep, $version, $result, $parent, $threadPool) {
                if ($pkg = $this->repo->findPackage($dep, $version, $this->packageLock)) {
                    $result->addDep($pkg, $this->fetchDependencyTree($pkg, '', $result, $threadPool));
                } else {
                    if ($parent) {
                        $parent->addInvalidDep($dep, $version);
                    }

                    $result->addInvalidDep($dep, $version);
                }
            };

            if ($threadPool) {
                $threadPool->execute($handler);
            } else {
                $handler();
            }
        }

        return $result;
    }

    /**
     * @param Package $package
     * @param string $directory
     */
    public function writePackage(Package $package, string $directory)
    {
        $file = $directory . "/" . Package::FILENAME;

        fs::formatAs($file, $package->toArray(), 'yaml', YamlProcessor::SERIALIZE_PRETTY_FLOW | YamlProcessor::SERIALIZE_NOT_SPLIT_LINES);
    }

    /**
     * @return Repository
     */
    public function getRepo(): Repository
    {
        return $this->repo;
    }
}