<?php

use php\io\File;
use php\io\FileStream;
use php\lang\Environment;
use php\lang\Module;
use php\lib\fs;

function ____makePackage($fileOrStream)
{
    $pkg = new \php\lang\Package();
    $type = 0;

    $sc = new \php\util\Scanner($fileOrStream instanceof \php\io\Stream ? $fileOrStream : fs::get($fileOrStream));

    $classes = [];
    $functions = [];
    $constants = [];

    while ($sc->hasNextLine()) {
        $line = \php\lib\str::trim($sc->nextLine());

        if ($line) {
            if ($line[0] == '[') {
                switch (\php\lib\str::trim($line)) {
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

function ____compile($sourceDir)
{
    $includedFiles = [];

    // Add packages -------------------------------
    fs::scan("$sourceDir/.packages", function ($filename) {
        $ext = fs::ext($filename);

        if ($ext == 'pkg') {
            $package = ____makePackage($filename);
            Environment::current()->setPackage(fs::nameNoExt($filename), $package);
        }
    }, 1);

    ob_implicit_flush(true);

    spl_autoload_register(function ($name) use ($sourceDir, &$includedFiles) {
        echo("Try class '$name' auto load\n");

        $filename = "$sourceDir/$name.php";
        $includedFiles[fs::normalize($filename)] = true;

        if (fs::exists($filename)) {
            echo "Find class '$name' in ", $filename, "\n";

            $compiled = new File($sourceDir, $name . ".phb");
            fs::ensureParent($compiled);

            $fileStream = new FileStream($filename);
            $module = new Module($fileStream, false, true);
            $module->dump($compiled, true);
            $fileStream->close();
            return;
        }
    });

    fs::scan($sourceDir, function ($filename) use (&$includedFiles) {
        if (fs::name($filename) === "__compile-app-bytecode.php") return;

        if (fs::ext($filename) === "php") {
            $compiledFile = fs::pathNoExt($filename) . '.phb';
            $filename = fs::normalize($filename);

            if ($includedFiles[$filename]) {
                return;
            }

            $includedFiles[$filename] = true;

            $fileStream = new FileStream($filename);
            $stream = new FileStream($compiledFile, 'w+');
            $module = new Module($fileStream, false, true);

            $module->dump($stream, true);
            $stream->close();
            $fileStream->close();
        } else {
            // skip.
        }
    });

    fs::clean($sourceDir, ['extensions' => ['php']]);
}

foreach (spl_autoload_functions() as $autoload_function) {
    spl_autoload_unregister($autoload_function);
}

____compile($sourceDir = \php\lang\System::getEnv()['compiler-source-dir']);