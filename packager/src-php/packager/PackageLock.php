<?php
namespace packager;
use packager\cli\Console;
use php\format\ProcessorException;
use php\format\YamlProcessor;
use php\io\IOException;
use php\lib\fs;

/**
 * Class PackageLock
 * @package packager
 */
class PackageLock
{
    /**
     * @var array[]
     */
    private $dependencies = [];

    public function addPackage(Package $pkg)
    {
        $this->dependencies[$pkg->getName()] = [
            "version" => $pkg->getVersion(),
            "info" => $pkg->getInfo(),
        ];
    }

    public function removePackage($pkgName): bool
    {
        if ($this->dependencies[$pkgName]) {
            unset($this->dependencies[$pkgName]);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param string $pkgName
     * @return null|string
     */
    public function findVersion(string $pkgName): ?string
    {
        return $this->dependencies[$pkgName]['version'] ?: null;
    }

    /**
     * @param string $pkgName
     * @return array|null
     */
    public function findVersionInfo(string $pkgName): ?array
    {
        return $this->dependencies[$pkgName]['info'] ?: null;
    }

    public function load(string $dir)
    {
        if (fs::isFile("$dir/package-lock.php.yml")) {
            try {
                $data = fs::parse("$dir/package-lock.php.yml");
                $this->dependencies = $data['dependencies'];
            } catch (ProcessorException|IOException $e) {
                Console::log("Warning, failed to load '$dir/package-lock.php.yml', {0}", $e->getMessage());
            }
        }
    }

    public function save(string $dir)
    {
        fs::makeDir($dir);
        fs::formatAs("$dir/package-lock.php.yml", [
            'dependencies' => $this->dependencies
        ], 'yaml', YamlProcessor::SERIALIZE_PRETTY_FLOW);
    }
}