<?php
namespace packager;

use packager\cli\Console;
use php\format\YamlProcessor;
use php\io\IOException;
use php\io\Stream;
use php\lang\System;
use php\lib\fs;
use php\lib\str;

/**
 * Class Packager
 * @package packager
 */
class Packager
{
    const VERSION = "0.0.1";

    /**
     * @var Repository
     */
    private $repo;

    /**
     * @var PackageLoader
     */
    private $packageLoader;

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
        $this->packageLoader = new PackageLoader();
    }

    public function install(Package $source, string $vendorDir)
    {
        fs::makeDir($vendorDir);

        $tree = $this->fetchDependencyTree($source);
        $this->packageLoader->clean();
        $this->packageLoader->registerPackage($source);

        $tree->eachDep(function (Package $pkg, PackageDependencyTree $tree, int $depth = 0) use ($vendorDir) {
            $prefix = str::repeat('-', $depth);

            Console::log("{$prefix}-> install {0}", $pkg->toString());

            $this->repo->copyTo($pkg, $vendorDir);
            $this->packageLoader->registerPackage($pkg);
        });

        $this->packageLoader->saveAutoload($vendorDir);
    }

    /**
     * @param Package $package
     * @param PackageDependencyTree $parent
     * @return PackageDependencyTree
     */
    public function fetchDependencyTree(Package $package, ?PackageDependencyTree $parent = null): PackageDependencyTree
    {
        $result = new PackageDependencyTree($package, $parent);

        foreach ($package->getDeps() as $dep => $version) {
            if ($parent->findByName($dep)) {
                continue;
            }

            if ($pkg = $this->repo->findPackage($dep, $version)) {
                $result->addDep($pkg, $this->fetchDependencyTree($pkg, $result));
            } else {
                $result->addInvalidDep($dep, $version);
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

        fs::formatAs($file, $package->toArray(), 'yaml', YamlProcessor::SERIALIZE_PRETTY_FLOW);
    }

    /**
     * @return Repository
     */
    public function getRepo(): Repository
    {
        return $this->repo;
    }
}