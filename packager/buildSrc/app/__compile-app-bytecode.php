<?php

use php\io\File;
use php\io\FileStream;
use php\io\Stream;
use php\lang\Environment;
use php\lang\Module;
use php\lang\Package;
use php\lang\System;
use php\lib\fs;
use php\lib\str;
use php\util\Scanner;

function ____makePackage($fileOrStream)
{
    $pkg = new Package();
    $type = 0;

    $sc = new Scanner($fileOrStream instanceof Stream ? $fileOrStream : fs::get($fileOrStream));

    $classes = [];
    $functions = [];
    $constants = [];

    while ($sc->hasNextLine()) {
        $line = str::trim($sc->nextLine());

        if ($line) {
            if ($line[0] == '[') {
                switch (str::trim($line)) {
                    case "[classes]":
                        $type = 1;
                        break;
                    case "[functions]":
                        $type = 2;
                        break;
                    case "[constants]":
                        $type = 3;
                        break;
                }
            } else {
                switch ($type) {
                    case 1:
                        $classes[] = $line;
                        break;
                    case 2:
                        $functions[] = $line;
                        break;
                    case 3:
                        $constants[] = $line;
                        break;

                }
            }
        }
    }

    if ($classes) {
        $pkg->addClasses($classes);
    }

    if ($functions) {
        $pkg->addFunctions($functions);
    }

    if ($constants) {
        $pkg->addConstants($constants);
    }

    return $pkg;
}

// ------------------------- COMPILE FUNCTION ------------------------------------ //

function ____compilePHBFile($filename, $target) {
    fs::ensureParent($target);
    $fileStream = new FileStream($filename);
    $module = new Module($fileStream, false, true);
    $module->dump($target, true, true);

    $fileStream->close();
}

function ____compileJVMFile($filename, $target) {
    fs::ensureParent($target);

    $fileStream = new FileStream($filename);
    $module = new Module($fileStream, false, true);
    $result = $module->dumpJVMClasses($target);

    $result['moduleName'] = $module->getName();

    $fileStream->close();

    return $result;
}

function ____compile($sourceDir, ?string $jvmClassesTargetDir = null)
{
    $includedFiles = [];
    $result = [];

    if ($jvmClassesTargetDir != null) {
        fs::makeDir($jvmClassesTargetDir);
    }

    // Add packages -------------------------------
    fs::scan("$sourceDir/.packages", function ($filename) {
        $ext = fs::ext($filename);

        if ($ext == 'pkg') {
            $package = ____makePackage($filename);
            Environment::current()->setPackage(fs::nameNoExt($filename), $package);
        }
    }, 1);

    ob_implicit_flush(true);

    spl_autoload_register(function ($name) use ($sourceDir, &$includedFiles, $jvmClassesTargetDir, &$result) {
        echo("Try class '$name' auto load\n");

        $filename = "$sourceDir/" . str::replace($name, "\\", "/") . ".php";

        if (fs::isFile($filename)) {
            echo "Find class '$name' in ", $filename, "\n";
            $includedFiles[fs::normalize($filename)] = true;

            if ($jvmClassesTargetDir) {
                $result[] = ____compileJVMFile($filename, $jvmClassesTargetDir);
            } else {
                ____compilePHBFile($filename, new File($sourceDir, str::replace($name, "\\", "/") . ".phb"));
            }

            $includedFiles[fs::normalize($filename)] = true;
            return;
        } else {
            echo "[WARN] Unable to load '$name' class, '$filename' is not found";
        }
    });

    fs::scan($sourceDir, function ($filename) use (&$includedFiles, $jvmClassesTargetDir, &$result) {
        if (fs::name($filename) === "__compile-app-bytecode.php") return;

        if (fs::ext($filename) === "php") {
            $compiledFile = fs::pathNoExt($filename) . '.phb';
            $filename = fs::normalize($filename);

            if ($includedFiles[$filename]) {
                return;
            }

            $includedFiles[$filename] = true;

            if ($jvmClassesTargetDir) {
                $result[] = ____compileJVMFile($filename, $jvmClassesTargetDir);
            } else {
                ____compilePHBFile($filename, $compiledFile);
            }
        } else {
            // skip.
        }
    });

    fs::clean($sourceDir, ['extensions' => ['php']]);
    return $result;
}

foreach (spl_autoload_functions() as $autoload_function) {
    spl_autoload_unregister($autoload_function);
}

$env = System::getEnv();
$result = ____compile($sourceDir = $env['compiler-source-dir'], $jvmClassesTargetDir = $env['compiler-jvm-target-dir'] ?: null);

if ($result && $jvmClassesTargetDir) {
    Stream::putContents("$jvmClassesTargetDir/____compile-result.tmp", serialize($result));
}