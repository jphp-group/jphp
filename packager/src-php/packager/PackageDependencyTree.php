<?php
namespace packager;

use php\lib\str;

/**
 * Class PackageDependencyTree
 * @package packager
 */
class PackageDependencyTree
{
    /**
     * @var Package
     */
    private $root;

    /**
     * @var Package[]
     */
    private $deps = [];

    /**
     * @var array
     */
    private $invalidDeps = [];

    /**
     * @var PackageDependencyTree
     */
    private $parent;

    /**
     * PackageDependencyTree constructor.
     * @param Package $root
     * @param null|PackageDependencyTree $parent
     */
    public function __construct(Package $root, ?PackageDependencyTree $parent = null)
    {
        $this->root = $root;
        $this->parent = $parent;
    }

    /**
     * @param Package $package
     * @param PackageDependencyTree $tree
     */
    public function addDep(Package $package, PackageDependencyTree $tree)
    {
        $this->deps[$package->getName()] = [$package, $tree];
    }

    /**
     * @param string $name
     * @param string $version
     * @param string|array $comment
     * @param bool $fail
     */
    public function addInvalidDep(string $name, string $version, $comment, bool $fail = false)
    {
        $this->invalidDeps[$name] = [$version, $comment, $fail];
    }

    /**
     * @return array
     */
    public function getInvalidDeps(): array
    {
        return $this->invalidDeps;
    }

    /**
     * @param callable $handler
     * @param int $depth
     */
    public function eachDep(callable $handler, int $depth = 0)
    {
        /**
         * @var Package $pkg
         * @var PackageDependencyTree $tree
         */
        foreach ($this->deps as [$pkg, $tree]) {
            $handler($pkg, $tree, $depth);

            $tree->eachDep($handler, $depth + 1);
        }
    }

    /**
     * @return array[]
     */
    public function getDeps(): array
    {
        return $this->deps;
    }

    /**
     * @param string $name
     * @return null|Package
     */
    public function findByName(string $name): ?Package
    {
        if ($dep = $this->deps[$name]) {
            return $dep[0];
        } else {
            /**
             * @var Package $pkg
             * @var PackageDependencyTree $tree
             */
            foreach ($this->deps as [$pkg, $tree]) {
                if ($pkg = $tree->findByName($name)) {
                    return $pkg;
                }
            }

            return null;
        }
    }

    /**
     * @param int $level
     * @return string
     */
    public function toString(int $level = 0): string
    {
        $result = "";

        $prefix = str::repeat("-", $level);

        /**
         * @var Package $pkg
         * @var PackageDependencyTree $tree
         */
        foreach ($this->deps as [$pkg, $tree]) {
            $result .= "{$prefix}-> {$pkg->getName()}@{$pkg->getVersion('last')}\n";
            $result .= $tree->toString($level + 1);
        }

        return $result;
    }
}