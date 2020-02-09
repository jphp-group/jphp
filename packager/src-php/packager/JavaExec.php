<?php
namespace packager;

use php\io\File;
use php\lang\Process;
use php\lib\fs;
use php\lib\str;

class JavaExec
{
    /**
     * @var array
     */
    private $classPaths = [];

    /**
     * @var array
     */
    private $jvmArgs = [];

    /**
     * @var string
     */
    private $javaBin = "java";

    /**
     * @var array
     */
    private $environment = null;

    /**
     * @var array
     */
    private $systemProperties = [];

    /**
     * @var string
     */
    private $mainClass;

    /**
     * JavaExec constructor.
     * @param string $mainClassOrJar
     * @param array $jvmArgs
     * @param array|null $environment
     */
    public function __construct(string $mainClassOrJar = 'php.runtime.launcher.Launcher', array $jvmArgs = [], array $environment = null)
    {
        $this->mainClass = $mainClassOrJar;
        $this->jvmArgs = $jvmArgs;
        $this->environment = $environment;

        $javaBin = "java";

        if ($_ENV['JAVA_HOME']) {
            $javaBin = $_ENV['JAVA_HOME'] . '/bin/java';

            if (fs::isFile("$javaBin.exe")) {
                $javaBin = "$javaBin.exe";
            }
        }

        $this->javaBin = $javaBin;
        $this->environment = $environment;
    }

    /**
     * @return array
     */
    public function getEnvironment(): array
    {
        return $this->environment;
    }

    /**
     * @param array $environment
     * @return JavaExec
     */
    public function setEnvironment(array $environment): JavaExec
    {
        $this->environment = $environment;
        return $this;
    }

    /**
     * @return string
     */
    public function getMainClass(): string
    {
        return $this->mainClass;
    }

    /**
     * @param string $mainClass
     * @return JavaExec
     */
    public function setMainClass(string $mainClass): JavaExec
    {
        $this->mainClass = $mainClass;
        return $this;
    }

    /**
     * @return string
     */
    public function getJavaBin(): string
    {
        return $this->javaBin;
    }

    /**
     * @param string $javaBin
     * @return JavaExec
     */
    public function setJavaBin(string $javaBin): JavaExec
    {
        $this->javaBin = $javaBin;
        return $this;
    }

    /**
     * @return array
     */
    public function getClassPaths(): array
    {
        return $this->classPaths;
    }

    /**
     * @param array $classPaths
     * @return JavaExec
     */
    public function setClassPaths(array $classPaths): JavaExec
    {
        $this->classPaths = $classPaths;
        return $this;
    }

    /**
     * @return array
     */
    public function getSystemProperties(): array
    {
        return $this->systemProperties;
    }

    /**
     * @param array $systemProperties
     * @return JavaExec
     */
    public function setSystemProperties(array $systemProperties): JavaExec
    {
        $this->systemProperties = $systemProperties;
        return $this;
    }

    /**
     * @return array
     */
    public function getJvmArgs(): array
    {
        return $this->jvmArgs;
    }

    /**
     * @param array $jvmArgs
     * @return JavaExec
     */
    public function setJvmArgs(array $jvmArgs): JavaExec
    {
        $this->jvmArgs = $jvmArgs;
        return $this;
    }

    /**
     * @param string $classPath
     * @return JavaExec
     */
    public function addClassPath(string $classPath): JavaExec
    {
        $this->classPaths[] = $classPath;
        return $this;
    }

    /**
     * @param Package $package
     * @return JavaExec
     */
    public function addPackageClassPath(Package $package): JavaExec
    {
        $classPaths = [];

        $jars = fs::scan("./jars/", ['extensions' => ['jar'], 'excludeDirs' => true], 1);

        if ($jars) {
            foreach ($jars as $jar) {
                $classPaths[] = fs::abs("./jars/" . fs::name($jar));
            }
        }

        if ($sources = $package->getSources()) {
            foreach ($sources as $src) {
                $classPaths[] = fs::abs("./$src");
            }
        }

        if ($classPaths) {
            $this->classPaths = flow($this->classPaths, $classPaths)->toArray();
        }

        return $this;
    }

    /**
     * @param Vendor $vendor
     * @param string $profile
     * @return JavaExec
     */
    public function addVendorClassPath(Vendor $vendor, string $profile = ''): JavaExec
    {
        $classPaths = flow($vendor->fetchPaths()['classPaths'][$profile])
            ->map(function ($cp) use ($vendor) { return "{$vendor->getDir()}/$cp"; })
            ->toArray();

        if ($classPaths) {
            $this->classPaths = flow($this->classPaths, $classPaths)->toArray();
        }

        return $this;
    }

    /**
     * @param array $args
     * @param string|null $directory
     * @return Process
     */
    public function run(array $args = [], string $directory = null)
    {
        $sysArgs = [];

        foreach ($this->systemProperties as $key => $value) {
            $sysArgs[] = "-D$key=$value";
        }

        if (str::endsWith($this->mainClass, '.jar')) {
            $commands = flow(
                [$this->javaBin, '-jar'],
                $this->jvmArgs, $sysArgs, [$this->mainClass], $args
            )->toArray();
        } else {
            $commands = flow(
                [$this->javaBin, '-cp', str::join($this->classPaths, File::PATH_SEPARATOR)],
                $this->jvmArgs, $sysArgs, [$this->mainClass], $args
            )->toArray();
        }

        $process = new Process(
            $commands,
            $directory,
            $this->environment
        );

        return $process;
    }
}