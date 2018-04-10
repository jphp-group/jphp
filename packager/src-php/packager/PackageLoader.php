<?php
namespace packager;
use php\format\JsonProcessor;
use php\lib\fs;

/**
 * Class PackageLoader
 * @package packager
 */
class PackageLoader
{
    private $classPaths = [];

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

        foreach ($package->getJars() as $jar) {
            $this->classPaths[$scope][] = "$dir/" . ($vendor === null ? "../jars/$jar" : $vendor->getRelativeFile($package, "jars/$jar"));
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
            "{$vendor->getDir()}/classPaths.json", $this->getClassPaths(),
            'json',
            JsonProcessor::SERIALIZE_PRETTY_PRINT
        );
    }
}