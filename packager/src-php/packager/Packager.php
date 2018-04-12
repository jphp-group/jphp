<?php

namespace packager;

use packager\cli\Console;
use php\format\YamlProcessor;
use php\io\IOException;
use php\io\Stream;
use php\lang\System;
use php\lib\fs;
use php\lib\str;
use Tasks;

/**
 * Class Packager
 * @package packager
 */
class Packager
{
    const VERSION = "0.1.1";

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
        $dir = System::getProperty('user.home') . '/.jppm';

        if (!fs::isDir($dir)) {
            fs::makeDir($dir);
        }

        $this->repo = new Repository("$dir/repo");
        $this->ignore = Ignore::ofFile("./.jppmignore");
        $this->ignore->setBase('/')
            ->addRule('/vendor/**')
            ->addRule('/package-lock.php.yml');

        $this->packageLoader = new PackageLoader();
        $this->packageLock = new PackageLock();
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

        $tree = $this->fetchDependencyTree($source);
        $devTree = $this->fetchDependencyTree($source, 'dev');

        $this->packageLock->load($vendor->getDir() . "/../");
        $this->packageLoader->clean();

        $usedPackages = [];

        foreach (['' => $tree, 'dev' => $devTree] as $scope => $one) {
            $one->eachDep(function (Package $pkg, PackageDependencyTree $tree, int $depth = 0) use ($forceUpdate, $vendor, $scope, &$usedPackages) {
                $usedPackages[$pkg->getName()] = true;
                $prefix = str::repeat('-', $depth);

                switch ($pkg->getType()) {
                    case "std":
                        if ($forceUpdate || !$vendor->alreadyInstalled($pkg, $this->packageLock)) {
                            Console::log("{$prefix}-> install {0}", $pkg->toString());

                            $this->repo->copyTo($pkg, $vendor->getDir());
                        }

                        $this->packageLock->addPackage($pkg);
                        break;
                }

                $this->packageLoader->registerPackage($pkg, $vendor, $scope);
            });
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
     * @return PackageDependencyTree
     */
    public function fetchDependencyTree(Package $package, string $scope = '', ?PackageDependencyTree $parent = null): PackageDependencyTree
    {
        $result = new PackageDependencyTree($package, $parent);

        $deps = ['' => $package->getDeps(), 'dev' => $package->getDevDeps()];

        foreach ($deps[$scope] as $dep => $version) {
            if ($parent && $parent->findByName($dep)) {
                continue;
            }

            if (str::startsWith($version, "./")) {
                if (fs::isFile("$version/" . Package::FILENAME)) {
                    $pkg = $this->repo->readPackage("$version/" . Package::FILENAME, [
                        'type' => 'dir',
                        'src' => $version
                    ]);

                    $result->addDep($pkg, $this->fetchDependencyTree($pkg, '', $result));
                } else {
                    $result->addInvalidDep($dep, $version);
                }
            }  else {
                if ($pkg = $this->repo->findPackage($dep, $version, $this->packageLock)) {
                    $result->addDep($pkg, $this->fetchDependencyTree($pkg, '', $result));
                } else {
                    $result->addInvalidDep($dep, $version);
                }
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