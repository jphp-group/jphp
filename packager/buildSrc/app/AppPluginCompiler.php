<?php
namespace app;

use packager\cli\Console;
use packager\JavaExec;
use packager\Package;
use packager\Vendor;
use php\io\DataStream;
use php\io\File;
use php\io\FileStream;
use php\io\Stream;
use php\lang\Environment;
use php\lang\Module;
use php\lib\fs;
use php\lib\str;

/**
 * Class AppPluginCompiler
 * @package app
 */
class AppPluginCompiler
{
    public string $dir;
    public string $jvmTargetDir;

    /**
     * AppPluginCompiler constructor.
     * @param string $dir
     * @param string|null $jvmTargetDir
     */
    public function __construct(string $dir, string $jvmTargetDir = null)
    {
        $this->dir = $dir;
        $this->jvmTargetDir = $jvmTargetDir;
    }
    
    protected function compileLibraryDump($compileResult)
    {
        $libraryDump = "$this->dir/JPHP-INF/library.dump";
        fs::ensureParent($libraryDump);

        $stream = Stream::of($libraryDump, "w+");
        $data = new DataStream($stream);

        $data->writeInt(5);
        $data->writeUTF("\1\7\4\3\3");

        $data->writeInt(str::length(JPHP_VERSION));
        $data->writeUTF(JPHP_VERSION);

        $data->writeInt(6);
        $data->writeUTF("7.1.99");

        $data->writeInt(count($compileResult));

        foreach ($compileResult as $one) {
            $module = $one['module'];
            $moduleName = fs::relativize($one['moduleName'], $this->dir);
            $moduleDump = $one['moduleDump'];

            $data->writeInt(str::length($moduleName));
            $data->writeUTF($moduleName);

            $internalName = fs::nameNoExt($moduleDump);
            $data->writeInt(str::length($internalName));
            $data->writeUTF($internalName);

            $data->writeInt(count($one['classes']));
            foreach ($one['classes'] as $name => $file) {
                $data->writeInt(str::length($name));
                $data->writeUTF($name);
            }

            $data->writeInt(count($one['functions']));
            foreach ($one['functions'] as $name => $file) {
                $data->writeInt(str::length($name));
                $data->writeUTF($name);
            }

            $data->writeInt(count($one['constants']));
            foreach ($one['constants'] as $name => $value) {
                $data->writeInt(str::length($name));
                $data->writeUTF($name);
            }
        }

        $stream->close();
    }

    public function compile(Package $package)
    {
        $exec = new JavaExec();

        fs::copy('res://app/__compile-app-bytecode.php', "$this->dir/__compile-app-bytecode.php");

        $vendor = new Vendor($package->getConfigVendorPath());
        $exec->addVendorClassPath($vendor);
        $exec->addPackageClassPath($package);
        $exec->addClassPath($this->dir);
        $exec->setEnvironment(['compiler-source-dir' => $this->dir, 'compiler-jvm-target-dir' => $this->jvmTargetDir]);
        $exec->setSystemProperties(['bootstrap.file' => 'res://__compile-app-bytecode.php', 'sourceDir' => $this->dir]);
        $exec->setMainClass('php.runtime.launcher.Launcher');

        $process = $exec->run([]);

        $status = $process->inheritIO()->startAndWait()->getExitValue();

        if ($status !== 0) {
            Console::error("Failed to build bytecode");
            exit($status);
        }

        fs::delete("$this->dir/__compile-app-bytecode.php");

        $resultFile = "$this->dir/____compile-result.tmp";
        if (fs::isFile($resultFile)) {
            $compileResult = unserialize(fs::get($resultFile));
            fs::delete($resultFile);
            
            $this->compileLibraryDump($compileResult);

            return $compileResult;
        }

        return [];
    }
}