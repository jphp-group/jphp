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
    private $sources = [];

    /**
     *
     */
    public function clean()
    {
        $this->sources = [];
    }

    /**
     * @param Package $package
     */
    public function registerPackage(Package $package)
    {
        $loaders = $package->getLoaders();

        foreach ($loaders as $type => $rules) {
            foreach ($rules as $prefix => $dirs) {
                $this->addLoader([$prefix => $dirs], $type);
            }
        }
    }

    /**
     * @param $rule
     * @param string $type
     */
    public function addLoader(array $rule, string $type = 'std')
    {
        foreach ($rule as $ns => $dirs) {
            if (!is_array($dirs)) {
                $dirs = [$dirs];
            }

            $this->sources[] = ['ns' => $ns, 'dirs' => $dirs, 'type' => $type];
        }
    }

    public function saveAutoload(string $vendorDir)
    {
        fs::copy('res://packager/loaders/autoload.php', "$vendorDir/autoload.php");
        fs::formatAs("$vendorDir/autoload.json", $this->sources, 'json', JsonProcessor::SERIALIZE_PRETTY_PRINT);
    }
}