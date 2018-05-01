<?php
namespace packager;
use php\format\JsonProcessor;
use php\lib\arr;
use php\lib\fs;

/**
 * Class PackageLoader
 * @package packager
 */
class PackageLoader
{
    private $classPaths = [];

    private $includes = [];

    /**
     *
     */
    public function clean()
    {
        $this->classPaths = [];
    }

    /**
     * @param Package $package
     * @param Vendor $vendor
     * @param string $scope
     */
    public function registerPackage(Package $package, ?Vendor $vendor = null, string $scope = '')
    {
        $dir = $package->getInfo()['dir'];

        foreach ($package->getSources() as $src) {
            if ($src[0] !== '/') {
                $this->classPaths[$scope][] = "$dir/" . ($vendor === null ? "../$src" : $vendor->getRelativeFile($package, $src));
            }
        }

        $jars = fs::scan(
            $tmp = ($dir ? "$dir/" : $dir) . ($vendor === null ? "../jars/" : $vendor->getFile($package, "jars/")),
            ['extensions' => ['jar'], 'excludeDirs' => true],
            1
        );

        foreach ($jars as $jar) {
            $jar = fs::name($jar);
            $this->classPaths[$scope][] = "$dir/" . ($vendor === null ? "../jars/$jar" : $vendor->getRelativeFile($package, "jars/$jar"));
        }

        foreach ((array)$package->getIncludes() as $include) {
            $this->includes[$include] = $include;
        }
    }

    /**
     * @return array
     */
    public function getClassPaths(): array
    {
        return $this->classPaths;
    }

    /**
     * @param Vendor $vendor
     */
    public function save(Vendor $vendor)
    {
        fs::formatAs(
            "{$vendor->getDir()}/paths.json",
                [
                    'classPaths' => $this->getClassPaths(),
                    'includes' => arr::values($this->includes)
                ],
            'json',
            JsonProcessor::SERIALIZE_PRETTY_PRINT
        );
    }
}