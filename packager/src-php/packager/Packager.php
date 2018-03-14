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
     * Packager constructor.
     */
    public function __construct()
    {
        $dir = System::getProperty('user.home') . '/.jppm';

        if (!fs::isDir($dir)) {
            fs::makeDir($dir);
        }

        $this->repo = new Repository("$dir/repo");
    }

    /**
     * @param Package $source
     * @param string $vendorDir
     * @param array $installed
     * @param int $depth
     */
    public function installVendors(Package $source, string $vendorDir, array $installed = [], int $depth = 0)
    {
        foreach ($source->getDeps() as $dep => $version) {
            if ($pkg = $this->repo->findPackage($dep, $version)) {
                $prefix = str::repeat('-', $depth);

                Console::log("{$prefix}-> install {0}@{1}", $pkg->getName(), $pkg->getVersion());

                $this->repo->copyTo($pkg, $vendorDir);

                $installedPkg = $installed[$source->getName()];

                if (!$installedPkg) {
                    $installed[$pkg->getName()] = $pkg;
                    // install deps.
                    $this->installVendors($pkg, $vendorDir, $installed, $depth + 1);
                }
            }
        }
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