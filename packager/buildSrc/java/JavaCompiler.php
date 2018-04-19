<?php
namespace java;

use packager\JavaExec;
use packager\Vendor;
use php\io\File;
use php\io\Stream;
use php\lang\Process;
use php\lib\fs;
use php\lib\str;

/**
 * Class JavaCompiler
 * @package java
 */
class JavaCompiler extends JavaExec
{
    /**
     * @var array
     */
    private $sourcePaths = [];

    /**
     * @var array
     */
    private $sourceFiles = [];

    /**
     * @var bool
     */
    private $verbose = false;

    /**
     * @var string
     */
    private $target = '1.8';

    /**
     * @var string
     */
    private $source = '1.8';

    /**
     * @var string
     */
    private $encoding = 'UTF-8';

    public function __construct($environment = null)
    {
        parent::__construct("", [], $environment);

        $javaBin = "javac";

        if ($_ENV['JAVA_HOME']) {
            $javaBin = $_ENV['JAVA_HOME'] . "/bin/javac";

            if (fs::isFile("$javaBin.exe")) {
                $javaBin = "$javaBin.exe";
            }
        }

        $this->setJavaBin($javaBin);
    }

    /**
     * @param string $path
     */
    public function addSourcePath(string $path)
    {
        $this->sourcePaths[$path] = $path;
    }

    /**
     * @param string $path
     */
    public function addSourcePathWithFiles(string $path)
    {
        $this->addSourcePath($path);

        fs::scan($path, ['extensions' => 'java', 'excludeDirs' => true, 'callback' => function ($filename) {
            $this->addSourceFile(fs::abs($filename));
        }], 512);
    }

    /**
     * @param string $filename
     */
    public function addSourceFile(string $filename)
    {
        $this->sourceFiles[$filename] = $filename;
    }

    /**
     * @return array
     */
    public function getSourceFiles(): array
    {
        return $this->sourceFiles;
    }

    /**
     * @param array $sourceFiles
     */
    public function setSourceFiles(array $sourceFiles)
    {
        $this->sourceFiles = $sourceFiles;
    }

    /**
     * @return string
     */
    public function getEncoding(): string
    {
        return $this->encoding;
    }

    /**
     * @param string $encoding
     */
    public function setEncoding(string $encoding)
    {
        $this->encoding = $encoding;
    }

    /**
     * @return array
     */
    public function getSourcePaths(): array
    {
        return $this->sourcePaths;
    }

    /**
     * @param array $sourcePaths
     */
    public function setSourcePaths(array $sourcePaths)
    {
        $this->sourcePaths = $sourcePaths;
    }

    /**
     * @return bool
     */
    public function isVerbose(): bool
    {
        return $this->verbose;
    }

    /**
     * @param bool $verbose
     */
    public function setVerbose(bool $verbose)
    {
        $this->verbose = $verbose;
    }

    /**
     * @return string
     */
    public function getSource(): string
    {
        return $this->source;
    }

    /**
     * @param string $source
     */
    public function setSource(string $source)
    {
        $this->source = $source;
    }

    /**
     * @return string
     */
    public function getTarget(): string
    {
        return $this->target;
    }

    /**
     * @param string $target
     */
    public function setTarget(string $target)
    {
        $this->target = $target;
    }

    public function compile(string $workDir, string $outDir): Process
    {
        $commands = flow([
            '-d ' . fs::abs($outDir),
        ]);

        if ($this->sourcePaths) {
            $commands->append(['-sourcepath ' . str::join($this->getSourcePaths(), File::PATH_SEPARATOR)]);
        }

        if ($this->getClassPaths()) {
            $commands->append(['-cp ' . str::join($this->getClassPaths(), File::PATH_SEPARATOR)]);
        }

        if ($this->verbose) {
            $commands->append(['-verbose']);
        }

        if ($this->target) {
            $commands->append(["-target $this->target"]);
        }

        if ($this->source) {
            $commands->append(["-source $this->target"]);
        }

        if ($this->encoding) {
            $commands->append(["-encoding $this->encoding"]);
        }

        $commands->append($this->getSourceFiles());

        $argfile = File::createTemp("javac-argfile", "");
        $argfile->deleteOnExit();
        Stream::putContents($argfile, $commands->toString("\n"));

        $process = new Process(
            [$this->getJavaBin(), "@$argfile"], $workDir,
            $this->getEnvironment()
        );

        return $process;
    }
}