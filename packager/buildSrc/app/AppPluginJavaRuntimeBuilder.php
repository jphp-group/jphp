<?php
namespace app;


use packager\cli\Console;
use packager\JavaExec;
use php\lang\Process;
use php\lang\System;
use php\lib\fs;
use php\lib\str;
use php\util\Flow;

class AppPluginJavaRuntimeBuilder
{
    /**
     * @var array
     */
    public $jars = [];

    public $jvmModules = [];

    public function __construct()
    {
    }

    public function addJar($jar)
    {
        $this->jars[$jar] = $jar;
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
        $jdepsBin = fs::parent($javaExec->getJavaBin()) . "/jdeps";

        $modules = [];

        foreach ($this->jars as $jar) {
            $proc = new Process([$jdepsBin, '-s', $jar], $workingDir);
            $proc = $proc->startAndWait();

            if ($proc->getExitValue() === 0) {
                $string = $proc->getInput()->readAll();
                foreach (str::lines($string, true) as $line) {
                    [, $module] = str::split($line, "->");
                    $module = str::trim($module);
                    if ($module === "not found") {
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

    public function build($workingDir)
    {
        $modules = flow(
            $this->fetchModules($workingDir),
            $this->jvmModules
        )->toMap();

        $javaExec = new JavaExec();
        $javaHome = fs::parent($javaExec->getJavaBin());
        $jlinkBin = $javaHome . "/jlink";

        $proc = new Process([
            $jlinkBin,
            "--module-path", "$javaHome/jmods;out",
            "--add-modules", str::join($modules, ","),
            "--output", "jre"
        ], $workingDir, System::getEnv());
        $proc = $proc->inheritIO()->startAndWait();

        if ($proc->getExitValue() !== 0) {
            exit(-1);
        }
    }
}