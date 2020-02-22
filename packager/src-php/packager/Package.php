<?php
namespace packager;

use php\io\IOException;
use php\io\Stream;
use php\lang\System;
use php\lib\arr;
use php\lib\str;
use php\util\Regex;

/**
 * Class Package
 * @package packager
 */
class Package
{
    const FILENAME = "package.php.yml";
    const FILENAME_S = "package-%s.php.yml";
    const LOCK_FILENAME = "package-lock.php.yml";

    const TYPE_LIBRARY = 'library';
    const TYPE_PROJECT = 'project';
    const TYPE_PLUGIN = 'plugin';
    const TYPE_META = 'meta';

    const TYPES = [self::TYPE_LIBRARY, self::TYPE_PROJECT, self::TYPE_PLUGIN, self::TYPE_META];

    /**
     * @var array
     */
    private $data = [];

    /**
     * @var array
     */
    private $info = [];

    /**
     * @var string
     */
    private static $os = '';

    /**
     * Set OS for build
     * @param string $os
     */
    public static function setOS(string $os)
    {
        self::$os = $os;
    }

    public static function isWindows(): bool
    {
        return self::$os === "win";
    }

    public static function isMac(): bool
    {
        return self::$os === "mac";
    }

    public static function isLinux(): bool
    {
        return self::$os === "linux";
    }

    /**
     * @return string linux|mac|win
     */
    public static function getOS(): string
    {
        return self::$os;
    }

    /**
     * Package constructor.
     * @param array $data
     * @param array $info
     */
    public function __construct(array $data, array $info = [])
    {
        $this->data = $data;
        $this->data = $this->prepareData($this->data);
        $this->data = $this->prepareDataForOS($this->data);

        $this->info = $info;
    }

    protected function prepareDataForOS(array $data): array
    {
        $depsLinux = flow((array) $data['depsUnix'], (array) $data['depsLinux'])->toMap();
        $depsWin = (array) $data['depsWin'];
        $depsMacOs = (array) $data['depsMac'];

        $deps = (array) $data['deps'];

        $osName = self::$os;

        if (str::posIgnoreCase($osName, 'win') > -1) {
            $deps = flow($deps, $depsWin)->toMap();
        } else if (str::posIgnoreCase($osName, 'mac') > -1) {
            $deps = flow($deps, $depsMacOs)->toMap();
        } else if ($osName) {
            $deps = flow($deps, $depsLinux)->toMap();
        }

        $data['deps'] = $deps;
        return $data;
    }

    protected function prepareData(array $data): array
    {
        $result = [];

        $pattern = new Regex('\\%([0-9a-z\\-\\_\\.\\:]+)\\%', 'i');

        foreach ($data as $key => $value) {
            if (is_array($value)) {
                $result[$key] = $this->prepareData($value);
            } else {
                $r = $pattern->with($value);

                $result[$key] = $r->replaceWithCallback(function (Regex $r) {
                    [$var, $def] = str::split($r->group(1), ':');

                    if (str::startsWith($var, 'env.')) {
                        return $_ENV[str::sub($var, 4)] ?? $def;
                    }

                    if (str::startsWith($var, 'sys.')) {
                        return System::getProperty(str::sub($var, 4), $def);
                    }

                    return $this->getAny($var, $def);
                });
            }
        }

        return $result;
    }

    public function getSize(): ?int
    {
        return $this->info['size'];
    }

    public function getHash(): ?string
    {
        return $this->info['sha256'] ?? $this->info['hash'];
    }

    /**
     * @return string
     */
    public function getType(): string
    {
        return $this->data['type'] ?: 'library';
    }

    public function getInfo(): array
    {
        return $this->info;
    }

    /**
     * @return string
     */
    public function getName(): string
    {
        return $this->data['name'];
    }

    /**
     * @return string
     */
    public function getNameWithVersion(): string
    {
        return $this->getName() . '@' . $this->getVersion('last');
    }

    /**
     * @param null|string $def
     * @return null|string
     */
    public function getVersion(string $def = null): ?string
    {
        return $this->data['version'] ?: $def;
    }

    /**
     * @return null|string
     */
    public function getRealVersion(): ?string
    {
        return $this->info['realVersion'] ?? $this->getVersion();
    }

    /**
     * @return null|string
     */
    public function getMain(): ?string
    {
        return $this->data['main'];
    }

    /**
     * @return array
     */
    public function getRepos(): array
    {
        return $this->data['repos'] ?: [];
    }

    /**
     * @return array
     */
    public function getJars(): array
    {
        return $this->data['jars'] ?: [];
    }

    /**
     * @return array
     */
    public function getDeps(): array
    {
        return $this->data['deps'] ?: [];
    }

    /**
     * @return array
     */
    public function getDevDeps(): array
    {
        return $this->data['devDeps'] ?: [];
    }

    public function toArray(): array
    {
        return $this->data;
    }

    public function getDescription(): ?string
    {
        return $this->data['description'] ?: null;
    }

    /**
     * @return array
     */
    public function getSources(): array
    {
        return $this->data['sources'] ?: [];
    }

    /**
     * @return array
     */
    public function getIncludes(): array
    {
        return $this->getAny('includes', []);
    }

    /**
     * @return array
     */
    public function getTasks(): array
    {
        return $this->getAny('tasks', []);
    }

    /**
     * @param string $key
     * @param null $def
     * @return mixed|null
     */
    public function getAny(string $key, $def = null)
    {
        $keys = str::split($key, '.');
        $keysCount = arr::count($keys);

        $result = $this->data;

        foreach ($keys as $k) {
            $keysCount--;

            if (isset($result[$k])) {
                $result = $result[$k];
            } else {
                return $def;
            }
        }

        if ($keysCount == 0) {
            return $result;
        } else {
            return $def;
        }
    }

    public function toString(): string
    {
        return $this->getName() . "@" . $this->getVersion('last');
    }

    /**
     * @return array
     */
    public function getProjects(): array
    {
        return $this->getAny('projects', []);
    }

    /**
     * @param string|Stream $source
     * @param array $info
     * @return Package
     */
    static public function readPackage($source, array $info = []): Package
    {
        $stream = $source instanceof Stream ? $source : Stream::of($source, 'r');

        try {
            $data = $stream->parseAs('yaml');

            return new Package((array) $data, $info);
        } finally {
            $stream->close();
        }
    }

    /**
     * @param Package $package
     * @return bool
     */
    public function isIdentical(Package $package)
    {
        return ($this->info == $package->info);
    }

    /**
     * @return string
     */
    public function getConfigVendorPath(): string
    {
        return $this->getAny('config.vendor-dir', './vendor');
    }

    /**
     * @return string
     */
    public function getConfigBuildPath(): string
    {
        return $this->getAny('config.build-dir', './build');
    }

    private $ignore = null;

    /**
     * @return Ignore
     */
    public function fetchIgnore(): Ignore
    {
        if ($this->ignore) return $this->ignore;

        $ignore = new Ignore((array) $this->getAny('config.ignore', []));

        $ignore->setBase('/');
        $ignore->addRule("!/" . self::FILENAME);
        $ignore->addRule("/" . self::LOCK_FILENAME);

        $ignore->addRule($this->getConfigBuildPath() . "/**");
        $ignore->addRule( $this->getConfigBuildPath() . "-*/**");
        $ignore->addRule($this->getConfigVendorPath() . "/**");
        $ignore->addRule("/package.*.yml");
        $ignore->addRule("/.gradle/**");
        $ignore->addRule("/gradle/**");
        $ignore->addRule("/src-jvm/**");
        $ignore->addRule("/build.gradle");
        $ignore->addRule("/gradlew.bat");
        $ignore->addRule("/gradlew");

        return $this->ignore = $ignore;
    }

    public function mergePackage(Package $profilePkg)
    {
        $this->data = arr::merge($this->data, $profilePkg->data);
    }
}