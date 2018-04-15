<?php

use compress\ZipArchive;
use compress\ZipArchiveEntry;
use packager\{
    cli\Console, Event, JavaExec, Packager, Vendor
};

use php\lang\{System, Thread};
use php\io\{
    File, Stream
};
use php\lib\fs;
use php\lib\str;
use php\time\Time;

# JPHP App Plugin.

/**
 * Class AppPlugin
 *
 * @jppm-task-prefix app
 *
 * @jppm-task clean
 * @jppm-task build
 * @jppm-task run
 */
class AppPlugin
{
    /**
     * AppPlugin constructor.
     */
    public function __construct(Event $event)
    {
        $event->packager()->getIgnore()->addRule('/build/**');
    }


    /**
     * @jppm-description Clean build directory.
     *
     * @param Event $event
     */
    function clean(Event $event)
    {
        Tasks::cleanDir("./build");
    }

    /**
     * @jppm-description Build app to executable jar file (with all dependencies).
     *
     * @jppm-depends-on app:clean
     * @jppm-depends-on install
     * @param Event $event
     */
    function build(Event $event)
    {
        $time = Time::millis();

        Console::log("Start building application ...");

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

        Tasks::createDir("./build/");
        Tasks::createFile("./build/$buildFileName");

        $zip = new ZipArchive("./build/$buildFileName");
        $zip->open();

        Tasks::createDir("./build/app");

        $metaInfServices = [];

        foreach ($exec->getClassPaths() as $classPath) {
            if (fs::isDir($classPath)) {
                Console::log("-> add dir: $classPath");

                fs::scan($classPath, function ($filename) use ($zip, $classPath, &$metaInfServices) {
                    $name = fs::relativize($filename, $classPath);
                    $file = "./build/app/$name";

                    Console::log("--> add file: {$classPath}{$name}");

                    if (str::startsWith($name, "META-INF/services/")) {
                        $metaInfServices[$name] = flow((array) $metaInfServices[$name], str::lines(fs::get($filename)))->toArray();
                    } else {
                        if (fs::isDir($filename)) {
                            fs::makeDir($file);
                        } else {
                            fs::ensureParent($file);
                            fs::copy($filename, $file);
                        }
                    }
                });
            } else if (fs::ext($classPath) === 'jar') {
                Console::log("-> add jar: $classPath");

                $jar = new ZipArchive($classPath);
                $jar->readAll(function (ZipArchiveEntry $stat, ?Stream $stream) use (&$metaInfServices) {
                    $name = $stat->name;

                    if ($stat->isDirectory()) {
                        return;
                    }

                    if (str::startsWith($name, "META-INF/services/")) {
                        $metaInfServices[$name] = flow((array) $metaInfServices[$name], str::lines($stream->readFully()))->toArray();
                    } else {
                        $file = "./build/app/{$name}";

                        //Console::log("--> add jar file: $name");

                        fs::ensureParent($file);
                        fs::copy($stream, $file);
                    }
                });
            }
        }

        Tasks::cleanDir("./build/app/JPHP-INF/sdk");
        Tasks::deleteFile("./build/app/JPHP-INF/sdk");
        fs::delete("./build/app/META-INF/manifest.mf");
        fs::delete("./build/app/META-INF/Manifest.mf");
        fs::delete("./build/app/META-INF/MANIFEST.MF");

        Tasks::createDir("./build/app/JPHP-INF/");
        Tasks::createDir("./build/app/META-INF/");

        fs::formatAs("./build/app/JPHP-INF/launcher.conf", [
            'bootstrap.file' => $launcher['bootstrap'] ? "res://{$launcher['bootstrap']}" : 'res://JPHP-INF/.bootstrap.php'
        ], 'ini');

        Tasks::createFile("./build/app/META-INF/MANIFEST.MF");

        Stream::putContents("./build/app/META-INF/MANIFEST.MF", str::join([
            "Manifest-Version: 1.0",
            "Created-By: jppm (JPHP Packager " . $event->packager()->getVersion() . ")",
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

        Console::log("\n   Use 'java -jar \"./build/$buildFileName\"' to run the result app.");

        Console::log("\n-----");
        Console::log("Building time: {0} sec.", round($time / 1000, 2));
        Console::log("Building is SUCCESSFUL. :)");
    }

    /**
     * @jppm-description Run app.
     *
     * @param Event $event
     * @jppm-depends-on install
     */
    function run(Event $event)
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

        $status = $process->inheritIO()->startAndWait()->getExitValue();

        $time = Time::millis() - $time;

        if ($metrics) {
            Console::log("\n--> Execute time: {0} sec.", round($time / 1000, 2));
        }

        exit($status);
    }
}