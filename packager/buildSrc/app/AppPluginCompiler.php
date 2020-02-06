<?php
namespace app;

use packager\JavaExec;
use packager\Package;
use packager\Vendor;
use php\io\File;
use php\io\FileStream;
use php\lang\Environment;
use php\lang\Module;
use php\lib\fs;

/**
 * Class AppPluginCompiler
 * @package app
 */
class AppPluginCompiler
{
    private $dir;

    /**
     * AppPluginCompiler constructor.
     * @param $dir
     */
    public function __construct($dir)
    {
        $this->dir = $dir;
    }

    public function compile(Package $package)
    {
        $exec = new JavaExec();

        fs::copy('res://app/__compile-app-bytecode.php', "$this->dir/__compile-app-bytecode.php");

        $vendor = new Vendor($package->getConfigVendorPath());
        $exec->addVendorClassPath($vendor);
        $exec->addPackageClassPath($package);
        $exec->addClassPath($this->dir);
        $exec->setEnvironment(['compiler-source-dir' => $this->dir]);
        $exec->setSystemProperties(['bootstrap.file' => 'res://__compile-app-bytecode.php', 'sourceDir' => $this->dir]);
        $exec->setMainClass('php.runtime.launcher.Launcher');

        $process = $exec->run([]);

        $status = $process->inheritIO()->startAndWait()->getExitValue();

        fs::delete("$this->dir/__compile-app-bytecode.php");
    }
}