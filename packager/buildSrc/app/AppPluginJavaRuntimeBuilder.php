<?php
namespace app;


use packager\cli\Console;
use packager\JavaExec;
use php\lang\Process;
use php\lang\System;
use php\lib\fs;
use php\lib\str;
use php\util\Flow;
use Tasks;
use function var_dump;

class AppPluginJavaRuntimeBuilder
{
    /**
     * @var array
     */
    public $jars = [];

    public $jvmModules = [];

    protected $javaHome;
    protected $javaHomeEmbedded;

    public function __construct()
    {
        $this->javaHome = fs::parent(fs::parent((new JavaExec())->getJavaBin()));
        $this->javaHomeEmbedded = $this->javaHome;
    }

    /**
     * @param string $javaHomeEmbedded
     */
    public function setJavaHomeEmbedded(string $javaHomeEmbedded): void
    {
        $this->javaHomeEmbedded = $javaHomeEmbedded;
    }

    /**
     * @param string $javaHome
     */
    public function setJavaHome(string $javaHome): void
    {
        $this->javaHome = $javaHome;
    }

    public function addJar($jar)
    {
        $this->jars[$jar] = fs::abs($jar);
    }

    public function addJvmModules(...$modules)
    {
        foreach ($modules as $module) {
            if (!$this->jvmModules[$module]) {
                $this->jvmModules[$module] = $module;
                Console::log("-- add jvm module '{0}'", $module);
            }
        }
    }

    public function fetchModules($workingDir)
    {
        $javaExec = new JavaExec();
        $jdepsBin = $this->javaHome . "/bin/jdeps";

        $modules = [];

        foreach ($this->jars as $jar) {
            $proc = new Process([$jdepsBin, '-s', $jar], $workingDir);
            $proc = $proc->startAndWait();

            if ($proc->getExitValue() === 0) {
                $string = $proc->getInput()->readAll();
                foreach (str::lines($string, true) as $line) {
                    [, $module] = str::split($line, "->");
                    $module = str::trim($module);
                    if (str::contains($module, " ")) {
                        continue;
                    }

                    if (!$modules[$module]) {
                        Console::log("-- add jvm module '{0}'", $module);
                        $modules[$module] = $module;
                    }
                }
            }
        }

        return $modules;
    }

    public function buildForJDK11($workingDir)
    {
        $modules = flow(
            $this->fetchModules($workingDir),
            $this->jvmModules
        )->toMap();

        $jlinkBin = $this->javaHome . "/bin/jlink";

        $proc = new Process([
            $jlinkBin,
            "--module-path", "$this->javaHomeEmbedded/jmods;out",
            "--add-modules", str::join($modules, ","),
            "--output", "jre"
        ], $workingDir, System::getEnv());
        $proc = $proc->inheritIO()->startAndWait();

        if ($proc->getExitValue() !== 0) {
            exit(-1);
        }
    }

    public function buildForJDK8($workingDir)
    {
        Tasks::copy("$this->javaHomeEmbedded/jre", "$workingDir/jre");
    }

    public function build($workingDir)
    {
        if (fs::exists("$this->javaHomeEmbedded/jmods")) {
            if (!fs::exists("$this->javaHome/jmods")) {
                Console::error("Failed to build jre, it's requires JDK 11+ with 'jlink' tool, JAVA_HOME = {0}", $this->javaHome);
                exit(-1);
            }

            $this->buildForJDK11($workingDir);
        } else {
            if (fs::exists("$this->javaHomeEmbedded/jre")) {
                $this->buildForJDK8($workingDir);
            } else {
                Console::error("Failed to build jre, cannot find {0}", $this->javaHomeEmbedded);
                exit(-1);
            }
        }
    }
}