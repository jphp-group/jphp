<?php
namespace plugins\app;

use packager\cli\Console;
use packager\cli\ConsoleApp;
use packager\Java;
use packager\Packager;
use packager\Plugin;
use packager\Vendor;
use php\compress\ZipFile;
use php\io\File;
use php\io\Stream;
use php\lang\Thread;
use php\lib\fs;
use php\lib\str;

class AppPlugin extends Plugin
{
    public function beforeConsole(ConsoleApp $consoleApp)
    {
        $consoleApp->addCommand('app:run', function ($args) use ($consoleApp) {
            $this->handleRun($consoleApp, $args);
        });

        $consoleApp->addCommand('app:build', function ($args) use ($consoleApp) {
            $this->handleBuild($consoleApp, $args);
        });
    }

    public function fetchClassPaths(Vendor $vendor): array
    {
        $classPaths = flow(fs::parseAs("{$vendor->getDir()}/classPaths.json", 'json')[''])
            ->map(function ($cp) use ($vendor) { return "{$vendor->getDir()}/$cp"; })
            ->toArray();

        if ($jars = $this->package->getJars()) {
            foreach ($jars as $jar) {
                $classPaths[] = fs::abs("./jars/$jar");
            }
        }

        if ($sources = $this->package->getSources()) {
            foreach ($sources as $src) {
                $classPaths[] = fs::abs("./$src");
            }
        }

        return $classPaths;
    }

    public function handleBuild(ConsoleApp $consoleApp, array $args)
    {
        $consoleApp->handleInstall([]);

        if (fs::isDir("./build")) {
            fs::clean("./build");
        } else {
            if (!fs::makeDir("./build")) {
                Console::log("Failed to create build dir: ./build/");
                exit(-1);
            }
        }

        $vendor = new Vendor("./vendor");
        $launcher = $this->package->getAny('app')['launcher'] ?: [];
        $build = $this->package->getAny('app')['build'] ?: [];

        $classPaths = $this->fetchClassPaths($vendor);

        $buildFileName = "{$this->package->getName()}-{$this->package->getVersion('last')}.jar";

        if ($build['fileName']) {
            $buildFileName = $build['fileName'];
        }

        $zip = new ZipFile("./build/$buildFileName", true);

        fs::makeDir("./build/app");

        $metaInfServices = [];

        foreach ($classPaths as $classPath) {
            if (fs::isDir($classPath)) {
                Console::log("-> add dir: $classPath");

                fs::scan($classPath, function ($filename) use ($zip, $classPath, &$metaInfServices) {
                    $name = fs::relativize($filename, $classPath);
                    $file = "./build/app/$name";

                    Console::log("--> add file: $name");

                    if (str::startsWith($name, "META-INF/services/")) {
                        $metaInfServices[$name] = flow((array) $metaInfServices[$name], str::lines(fs::get($filename)))->toArray();
                    } else {
                        fs::ensureParent($file);
                        fs::copy($filename, $file);
                    }
                });
            } else if (fs::ext($classPath) === 'jar') {
                Console::log("-> add jar: $classPath");

                $jar = new ZipFile($classPath, false);
                $jar->readAll(function (array $stat, Stream $stream) use (&$metaInfServices) {
                    $name = $stat['name'];

                    if ($stat['directory']) {
                        return;
                    }

                    if (str::startsWith($name, "META-INF/services/")) {
                        $metaInfServices[$name] = flow((array) $metaInfServices[$name], str::lines($stream->readFully()))->toArray();
                    } else {
                        //$file = "./build/app/{$name}";

                        //Console::log("--> add jar file: $name");

                        //fs::ensureParent($file);
                        //fs::copy($stream, $file);
                    }
                });

                $jar->unpack("./build/app");
            }
        }

        fs::clean("./build/app/JPHP-INF/sdk");
        fs::delete("./build/app/JPHP-INF/sdk");
        fs::delete("./build/app/META-INF/manifest.mf");
        fs::delete("./build/app/META-INF/Manifest.mf");
        fs::delete("./build/app/META-INF/MANIFEST.MF");

        fs::makeDir("./build/app/JPHP-INF/");
        fs::makeDir("./build/app/META-INF/");

        fs::formatAs("./build/app/JPHP-INF/launcher.conf", [
            'bootstrap.file' => $launcher['bootstrap'] ? "res://{$launcher['bootstrap']}" : 'res://JPHP-INF/.bootstrap.php'
        ], 'ini');

        Stream::putContents("./build/app/META-INF/MANIFEST.MF", str::join([
            "Manifest-Version: 1.0",
            "Created-By: jppm (JPHP Packager " . Packager::VERSION . ")",
            "Main-Class: " . ($launcher['mainClass'] ?: 'php.runtime.launcher.Launcher'),
            "",
            "",
        ], "\r\n"));

        foreach ($metaInfServices as $name => $lines) {
            fs::ensureParent("./build/app/$name");
            Stream::putContents("./build/app/$name", str::join($lines, "\n"));
        }

        $zip->addDirectory("./build/app");
    }

    public function handleRun(ConsoleApp $consoleApp, array $args)
    {
        $consoleApp->handleInstall([]);

        $launcher = $this->package->getAny('app')['launcher'] ?: [];

        $vendor = new Vendor("./vendor");
        $classPaths = $this->fetchClassPaths($vendor);

        $args = [
            '-cp', str::join($classPaths, File::PATH_SEPARATOR),
        ];

        if ($launcher['trace']) {
            $args[] = '-Djphp.trace=true';
        }

        $args[] = '-Dfile.encoding=' . ($launcher['encoding'] ?: 'UTF-8');

        if ($launcher['bootstrap']) {
            $args[] = '-Dbootstrap.file=res://' . $launcher['bootstrap'];
        }

        if (is_array($launcher['jvmArgs'])) {
            $args = flow($args, $launcher['jvmArgs'])->toArray();
        }

        $args[] = $launcher['mainClass'] ?: 'php.runtime.launcher.Launcher';

        if (is_array($launcher['args'])) {
            $args = flow($args, $launcher['args'])->toArray();
        }

        $process = Java::exec($args);

        $process = $process->start();

        $input = $process->getInput();
        $err = $process->getError();

        (new Thread(function () use ($input, $process) {
            $stdout = Stream::of('php://stdout');

            while (($ch = $input->read(1)) !== null) {
                $stdout->write($ch);
            }
        }))->start();

        $errThread = (new Thread(function () use ($err, $process) {
            $stdout = Stream::of('php://stderr');

            while (($ch = $err->read(1)) !== null) {
                $stdout->write($ch);
            }
        }));
        $errThread->start();

        $status = $process->waitFor();
        usleep(100000);
        exit($status);
    }
}