<?php

use compress\ZipArchive;
use packager\{
    cli\Console, Event, JavaExec, Packager, Vendor
};
use php\compress\ZipFile;
use php\lang\{System, Thread};
use php\io\{
    File, Stream
};
use php\lib\fs;
use php\lib\str;
use php\time\Time;

# JPHP App Plugin.

class AppPlugin
{
    static function clean(Event $event)
    {
        Console::log("Clean '{0}' directory.", fs::abs('./build'));

        if (fs::isDir("./build")) {
            fs::clean("./build");
        } else {
            if (!fs::makeDir("./build")) {
                Console::log("Failed to create build dir: ./build/");
                exit(-1);
            }
        }
    }

    static function build(Event $event)
    {
        $time = Time::millis();
        Console::log("Start building application ...");

        static::clean($event);

        $vendor = new Vendor("./vendor");
        $launcher = $event->package()->getAny('app') ?: [];
        $build = $event->package()->getAny('app')['build'] ?: [];

        $exec = new JavaExec();

        $exec->addVendorClassPath($vendor);
        $exec->addPackageClassPath($event->package());

        $buildFileName = "{$event->package()->getName()}-{$event->package()->getVersion('last')}.jar";

        if ($build['fileName']) {
            $buildFileName = $build['fileName'];
        }

        $zip = new ZipArchive("./build/$buildFileName");
        $zip->open();

        fs::makeDir("./build/app");

        $metaInfServices = [];

        foreach ($exec->getClassPaths() as $classPath) {
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

        fs::scan("./build/app", function (File $file) use ($zip) {
            if ($file->isFile()) {
                $zip->addFile($file, fs::relativize($file, "./build/app"));
            }
        });

        $zip->close();

        $time = Time::millis() - $time;

        Console::log("\n-----");
        Console::log("Building time: {0} sec.", round($time / 1000, 2));
        Console::log("Building is SUCCESSFUL. :)");
    }

    static function run(Event $event)
    {
        $exec = new JavaExec();

        $launcher = $event->package()->getAny('app') ?: [];

        $metrics = $launcher['metrics'];

        $vendor = new Vendor("./vendor");
        $exec->addVendorClassPath($vendor);
        $exec->addPackageClassPath($event->package());

        $sysArgs = [];

        if ($launcher['trace']) {
            $sysArgs['jphp.trace'] = 'true';
        }

        $sysArgs['file.encoding'] = $launcher['encoding'] ?: 'UTF-8';

        if ($launcher['bootstrap']) {
            $sysArgs['bootstrap.file'] = 'res://' . $launcher['bootstrap'];
        }

        if (is_array($launcher['jvmArgs'])) {
            $exec->setJvmArgs($launcher['jvmArgs']);
        }

        $exec->setSystemProperties($sysArgs);
        $exec->setMainClass($launcher['mainClass'] ?: 'php.runtime.launcher.Launcher');

        $process = $exec->run($launcher['args'] ?: []);

        $time = Time::millis();
        $process = $process->start();

        /*System::setIn($process->getInput());
        System::setOut($process->getOutput());
        System::setErr($process->getError());*/

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

        $time = Time::millis() - $time;
        usleep(100000);

        if ($metrics) {
            Console::log("\n--> Execute time: {0} sec.", round($time / 1000, 2));
        }

        exit($status);
    }
}