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
use semver\SemVersion;
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
     * @var PackageLoader
     */
    private $packageLoader;

    /**
     * @var PackageLock
     */
    private $packageLock;

    /**
     * @var string[]
     */
    private $profiles = ['dev'];

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

        foreach ($argv as $arg) {
            if (str::startsWith($arg, '-profile=')) {
                $this->profiles = str::split(str::split($arg, '=', 2)[1], ',');
                Console::info(
                    "Using profile '{0}'",
                    flow($this->profiles)
                        ->map(fn($el) => Colors::withColor($el, 'yellow'))
                        ->toString("' + '")
                );
                break;
            }
        }

        System::setProperty("jppm.home", $home);

        $this->version = fs::parse("$home/package.php.yml")['version'];

        $dir = System::getProperty('user.home') . '/.jppm';

        if (!fs::isDir($dir)) {
            fs::makeDir($dir);
        }

        $this->repo = new Repository("$dir/repo");

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
     * @return string[]
     */
    public function getProfiles(): array
    {
        return $this->profiles;
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

    public function loadPackageLock($dir)
    {
        $this->packageLock->load($dir);
    }

    public function removeDepFromPackageLock($depName)
    {
        return $this->packageLock->removePackage($depName);
    }

    public function install(Package $source, Vendor $vendor, bool $forceUpdate = false)
    {
        fs::makeDir($vendor->getDir());

        $thPool = null; //ThreadPool::create(4, 8);

        //$this->loadPackageLock($vendor->getDir() . "/../");

        $tree = $this->fetchDependencyTree($source, '', null, $thPool);
        $devTree = $this->fetchDependencyTree($source, 'dev', null, $thPool);

        if ($thPool instanceof ThreadPool) {
            while ($thPool->getActiveCount() > 0) {
                Timer::sleep('0.1s');
            }

            $thPool->shutdown();
        }

        $this->packageLoader->clean();

        $usedPackages = [];

        foreach (['' => $tree, 'dev' => $devTree] as $scope => $one) {
            /** @var PackageDependencyTree $one */
            $one->eachDep(function (Package $pkg, PackageDependencyTree $tree, int $depth = 0) use ($forceUpdate, $vendor, $scope, &$usedPackages) {
                if ($depth === 0 && $scope !== 'dev' && $pkg->getType() === Package::TYPE_PLUGIN) {
                    Console::warn("-> failed to install {0}, the package is a plugin and must be in 'devDeps'.", $pkg->getNameWithVersion());
                    return;
                }

                $usedPackages[$pkg->getName()] = true;
                $prefix = str::repeat('-', $depth);

                if ($forceUpdate || !$vendor->alreadyInstalled($pkg, $this->packageLock)) {
                    Console::log("{$prefix}-> install {0}@{1}", $pkg->getName(), $pkg->getRealVersion());

                    $this->repo->copyTo($pkg, $vendor->getDir());
                }

                $this->packageLock->addPackage($pkg);
                $this->packageLoader->registerPackage($pkg, $vendor, $scope);
            });

            $hasFail = false;
            foreach ($one->getInvalidDeps() as $name => list($version, $comment, $fail)) {
                $method = $fail ? 'warn' : 'error';

                if (!is_array($comment)) {
                    Console::{$method}("-> failed to install {0}@{1}, {2}.", $name, $version, $comment);
                } else {
                    Console::{$method}("-> failed to install {0}@{1}: ", $name, $version);
                    foreach ($comment as $c) {
                        Console::{$method}("  1. {0} ", $c);
                    }
                }

                if ($fail) $hasFail = true;
            }

            if ($hasFail) {
                exit(-1);
            }
        }

        /**
         * @var Package $pkg
         * @var PackageDependencyTree $tree
         */
        foreach ($devTree->getDeps() as [$pkg, $tree]) {
            if ($pkg->getAny('type') === Package::TYPE_PLUGIN) {
                $this->packageLoader->registerPlugin($pkg, $vendor);
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


    protected function checkPackageOs(Package $pkg, $os)
    {
        if ($os) {
            $osVariants = [];
            $osName = str::split(System::getProperty("os.name"), ' ')[0];
            $osVersion = System::getProperty("os.version");

            $success = flow($os)->anyMatch(function ($variant) use (&$osVariants, $osName, $osVersion) {
                if (is_array($variant)) {
                    $name = $variant['name'];
                    $version = $variant['version'];

                    $osVariants[] = "$name-$version";
                    if (str::equalsIgnoreCase($osName, $name)) {
                        return true;
                    }

                    return false;
                } else {
                    $osVariants[] = $variant;
                    [$name, $version] = str::split($variant, '-');

                    return str::equalsIgnoreCase($osName, $name);
                }
            });

            if (!$success) {
                $p1 = flow($osVariants)->toString(' or ');
                return "'{$pkg->getNameWithVersion()}' requires OS '{$p1}', but it's '{$osName}'";
            }
        }

        return null;
    }

    protected function checkPackageJava(Package $pkg, $java)
    {
        $type = $java['type'] ?? 'jre';
        $version = $java['version'] ?? null;
        $arch = $java['arch'] ?? null;

        if ($version) {
            $javaVersion = str::split(System::getProperty("java.version"), '_')[0];

            $javaVersion = new SemVersion($javaVersion);
            if (!$javaVersion->satisfies($version)) {
                return "'{$pkg->getNameWithVersion()}' requires Java version {$version}, but it's {$javaVersion}";
            }
        }

        if ($arch) {
            $osArch = System::getProperty("os.arch");

            $success = flow($arch)->anyMatch(function ($el) use ($osArch) {
                return $el === $osArch;
            });

            if (!$success) {
                $p1 = flow($arch)->toString(' or ');
                return "'{$pkg->getNameWithVersion()}' requires Java with '$p1' architecture(s), but it's $osArch";
            }
        }

        return null;
    }

    /**
     * @param Package $pkg
     * @return array errors of check
     */
    public function checkPackage(Package $pkg): array
    {
        $requires = $pkg->getAny('requires');

        $java = $requires['java'];
        $os = $requires['os'];

        $result = [];

        if (isset($java)) {
            if ($s = $this->checkPackageJava($pkg, $java)) {
                $result[] = $s;
            }
        }

        if (isset($os)) {
            if ($s = $this->checkPackageOs($pkg, $os)) {
                $result[] = $s;
            }
        }

        return $result;
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
                    $result->addDep($pkg, $this->fetchDependencyTree($pkg, '', $result));
                    if ($errors = $this->checkPackage($pkg)) {
                        if ($parent) {
                            $parent->addInvalidDep($dep, $version, $errors, true);
                        }

                        $result->addInvalidDep($dep, $version, $errors, true);
                    }
                } else {
                    if ($parent) {
                        $parent->addInvalidDep($dep, $version, 'cannot find package', true);
                    }

                    $result->addInvalidDep($dep, $version, 'cannot find package', true);
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