<?php
namespace app;

use packager\cli\Console;
use packager\Package;
use php\io\Stream;
use php\lang\Process;
use php\lang\System;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;

/**
 * Class AppPluginLaunch4JBuilder
 * @package app
 */
class AppPluginLaunch4JBuilder
{
    public $buildDir;

    /**
     * @var array
     */
    private $config;
    /**
     * @var Package
     */
    private $package;
    /**
     * @var string
     */
    private $headerType;
    /**
     * @var string
     */
    private $mainClass;

    public function __construct(Package $package, string $buildDir, array $config, string $mainClass, string $headerType = 'console')
    {
        $this->buildDir = $buildDir;
        $this->config = $config;
        $this->package = $package;
        $this->headerType = $headerType;
        $this->mainClass = $mainClass;
    }

    public function build()
    {
        $launch4j = $this->config['launch4j']['path'];
        if (is_array($launch4j)) {
            $launch4j = $launch4j[$this->package->getOS()];
        }

        $launch4jBin = "launch4j";
        if (Package::isWindows()) {
            $launch4jBin = "launch4jc.exe";
        }

        $launch4jBin = "$launch4j/$launch4jBin";
        if (!fs::isFile($launch4jBin)) {
            Console::error("Unable to find launch4j program to build 'exe' launcher, path to bin = {0}", $launch4jBin);
            exit(-1);
        }

        $launch4j_xml = fs::get("res://app/scripts/launch4j.xml");
        $launch4j_xml = str::replace($launch4j_xml, "{{headerType}}", $this->headerType);
        $launch4j_xml = str::replace($launch4j_xml, "{{stayAlive}}", $this->headerType === "console" ? "true" : "false"); // false for gui, true for console

        $launch4j_xml = str::replace($launch4j_xml, "{{dontWrapJar}}", "true");
        $launch4j_xml = str::replace($launch4j_xml, "{{errTitle}}", $this->package->getName());
        $launch4j_xml = str::replace($launch4j_xml, "{{supportUrl}}", "");
        $launch4j_xml = str::replace($launch4j_xml, "{{cmdLine}}", "");
        $launch4j_xml = str::replace($launch4j_xml, "{{chdir}}", ".");
        $launch4j_xml = str::replace($launch4j_xml, "{{restartOnCrash}}", "false");
        $launch4j_xml = str::replace($launch4j_xml, "{{icon}}", $this->config['icons']['win']);
        $launch4j_xml = str::replace($launch4j_xml, "{{mainClass}}", $this->mainClass);

        $mainJar = arr::first(fs::scan($this->buildDir, ['extensions' => ['jar']], 1));

        $cp = "";

        if ($mainJar) {
            $cp .= "<cp>./$mainJar</cp>";
        }

        if (fs::scan($this->buildDir . "/libs/", ['extensions' => ['jar']], 1)) {
            $cp .= '<cp>./libs/*.jar</cp>';
        }

        $launch4j_xml = str::replace($launch4j_xml, "{{cp}}", $cp);
        $launch4j_xml = str::replace($launch4j_xml, "{{singleInstance}}", "");
        $launch4j_xml = str::replace($launch4j_xml, "{{initialHeapSize}}", "64");
        $launch4j_xml = str::replace($launch4j_xml, "{{outfile}}", "{$this->package->getName()}-{$this->package->getVersion('last')}.exe");

        \Tasks::createFile("$this->buildDir/launch4j-config.xml", $launch4j_xml);

        $exec = new Process([$launch4jBin, "launch4j-config.xml", ""], $this->buildDir, System::getEnv());
        $exec = $exec->inheritIO()->startAndWait();

        if ($exec->getExitValue() !== 0) {
            exit($exec->getExitValue());
        }
    }
}