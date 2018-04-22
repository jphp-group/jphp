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
     * @jppm-dependency-of clean
     *
     * @jppm-need-package true
     * @jppm-description Clean build directory.
     *
     * @param Event $event
     */
    function clean(Event $event)
    {
        Tasks::cleanDir($event->package()->getConfigBuildPath());
    }

    /**
     * @jppm-description Build app to executable jar file (with all dependencies).
     *
     * @jppm-dependency-of build
     *
     * @jppm-need-package true
     *
     * @jppm-depends-on app:clean
     * @jppm-depends-on install
     * @param Event $event
     */
    function build(Event $event)
    {
        $time = Time::millis();

        //Console::log("Start building application ...");

        $vendor = new Vendor($event->package()->getConfigVendorPath());
        $launcher = $event->package()->getAny('app') ?: [];
        $build = $event->package()->getAny('app')['build'] ?: [];

        $exec = new JavaExec();

        $exec->addVendorClassPath($vendor);
        $exec->addPackageClassPath($event->package());

        $buildFileName = "{$event->package()->getName()}-{$event->package()->getVersion('last')}.jar";

        if ($build['fileName']) {
            $buildFileName = $build['fileName'];
        }

        $buildDir = $event->package()->getConfigBuildPath();

        Tasks::createDir("$buildDir");
        Tasks::createFile("$buildDir/$buildFileName");

        $zip = new ZipArchive("$buildDir/$buildFileName");
        $zip->open();

        Tasks::createDir("$buildDir/app");

        $metaInfServices = [];

        foreach ($exec->getClassPaths() as $classPath) {
            if (fs::isDir($classPath)) {
                Console::log("-> add dir: $classPath");

                fs::scan($classPath, function ($filename) use ($zip, $classPath, &$metaInfServices, $buildDir) {
                    $name = fs::relativize($filename, $classPath);
                    $file = "$buildDir/app/$name";

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
                $jar->readAll(function (ZipArchiveEntry $stat, ?Stream $stream) use (&$metaInfServices, $buildDir) {
                    $name = $stat->name;

                    if ($stat->isDirectory()) {
                        return;
                    }

                    if (str::startsWith($name, "META-INF/services/")) {
                        $metaInfServices[$name] = flow((array) $metaInfServices[$name], str::lines($stream->readFully()))->toArray();
                    } else {
                        $file = "$buildDir/app/{$name}";

                        //Console::log("--> add jar file: $name");

                        fs::ensureParent($file);
                        fs::copy($stream, $file);
                    }
                });
            }
        }

        Tasks::cleanDir("$buildDir/app/JPHP-INF/sdk");
        Tasks::deleteFile("$buildDir/app/JPHP-INF/sdk");

        fs::delete("$buildDir/app/META-INF/manifest.mf");
        fs::delete("$buildDir/app/META-INF/Manifest.mf");
        fs::delete("$buildDir/app/META-INF/MANIFEST.MF");

        if (!$launcher['disable-launcher']) {
            Console::log("-> create jphp app launcher");

            Tasks::createDir("$buildDir/app/JPHP-INF/");
            Tasks::createDir("$buildDir/app/META-INF/");

            fs::formatAs("$buildDir/app/JPHP-INF/launcher.conf", [
                'bootstrap.file' => $launcher['bootstrap'] ? "res://{$launcher['bootstrap']}" : 'res://JPHP-INF/.bootstrap.php'
            ], 'ini');

            Tasks::createFile("$buildDir/app/META-INF/MANIFEST.MF");

            Stream::putContents("$buildDir/app/META-INF/MANIFEST.MF", str::join([
                "Manifest-Version: 1.0",
                "Created-By: jppm (JPHP Packager " . $event->packager()->getVersion() . ")",
                "Main-Class: " . ($launcher['mainClass'] ?? 'php.runtime.launcher.Launcher'),
                "",
                "",
            ], "\r\n"));
        } else {
            Console::log("-> jphp app launcher is disable.");
        }

        foreach ($metaInfServices as $name => $lines) {
            fs::ensureParent("$buildDir/app/$name");
            Stream::putContents("$buildDir/app/$name", str::join($lines, "\n"));
        }

        fs::scan("$buildDir/app", function (File $file) use ($zip, $buildDir) {
            if ($file->isFile()) {
                $zip->addFile($file, fs::relativize($file, "$buildDir/app"));
            }
        });

        $zip->close();

        $time = Time::millis() - $time;

        if (!$launcher['disable-launcher']) {
            Console::log("\n   Use 'java -jar \"$buildDir/$buildFileName\"' to run the result app.");
        }

        Console::log("\n-----");
        Console::log("Building time: {0} sec.", round($time / 1000, 2));
        Console::log("Building is SUCCESSFUL. :)");
    }

    /**
     * @jppm-need-package true
     * @jppm-description Run app.
     *
     * @jppm-dependency-of start
     *
     * @param Event $event
     * @jppm-depends-on install
     */
    function run(Event $event)
    {
        $exec = new JavaExec();

        $launcher = $event->package()->getAny('app') ?: [];

        $metrics = $launcher['metrics'];

        $vendor = new Vendor($event->package()->getConfigVendorPath());
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