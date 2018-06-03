<?php

use compress\GzipOutputStream;
use compress\TarArchive;
use compress\TarArchiveEntry;
use compress\ZipArchive;
use compress\ZipArchiveEntry;
use packager\{
    cli\Console, Event, JavaExec, Packager, Vendor
};

use php\lang\{
    System, Thread
};
use php\io\{
    File, Stream
};
use php\lib\arr;
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
     * @throws \php\io\IOException
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

        $buildFileName = "{$event->package()->getName()}-{$event->package()->getVersion('last')}";

        if ($build['file-name']) {
            $buildFileName = $build['file-name'];
        }

        $buildType = $build['type'] ?? 'one-jar';

        $buildDir = $event->package()->getConfigBuildPath();

        Tasks::createDir("$buildDir");
        Tasks::createFile("$buildDir/$buildFileName.jar");

        $zip = new ZipArchive("$buildDir/$buildFileName.jar");
        $zip->open();

        Tasks::createDir("$buildDir/.app");

        $metaInfServices = [];
        $jars = [];

        foreach ($exec->getClassPaths() as $classPath) {
            if (fs::isDir($classPath)) {
                Console::log("-> add dir: $classPath");

                fs::scan($classPath, function ($filename) use ($zip, $classPath, &$metaInfServices, $buildDir) {
                    $name = fs::relativize($filename, $classPath);
                    $file = "$buildDir/.app/$name";

                    Console::log("--> add file: {$classPath}/{$name}");

                    if (str::startsWith($name, "META-INF/services/")) {
                        $metaInfServices[$name] = flow((array)$metaInfServices[$name], str::lines(fs::get($filename)))->toArray();
                    } else {
                        if (fs::isDir($filename)) {
                            fs::makeDir($file);
                        } else {
                            if (!fs::isFile($file)) {
                                fs::ensureParent($file);
                                fs::copy($filename, $file);
                            }
                        }
                    }
                });
            } else if (fs::ext($classPath) === 'jar') {
                Console::log("-> add jar: $classPath");

                switch ($buildType) {
                    case 'one-jar': {
                        $jar = new ZipArchive($classPath);
                        $jar->readAll(function (ZipArchiveEntry $stat, ?Stream $stream) use (&$metaInfServices, $buildDir) {
                            $name = $stat->name;

                            if ($stat->isDirectory()) {
                                return;
                            }

                            if (str::startsWith($name, "META-INF/services/")) {
                                $metaInfServices[$name] = flow((array)$metaInfServices[$name], str::lines($stream->readFully()))->toArray();
                            } else {
                                $file = "$buildDir/.app/{$name}";

                                //Console::log("--> add jar file: $name");

                                fs::ensureParent($file);
                                fs::copy($stream, $file);
                            }
                        });

                        break;
                    }

                    case 'multi-jar':
                        $jars[] = "./libs/" . fs::name($classPath);

                        Tasks::createDir("$buildDir/libs");
                        Tasks::copy($classPath, "$buildDir/libs");
                        break;

                    default:
                        Console::error("Unknown build.type variant: {0}", $buildType);
                        exit(-1);
                }
            }
        }

        Tasks::cleanDir("$buildDir/.app/JPHP-INF/sdk");
        Tasks::deleteFile("$buildDir/.app/JPHP-INF/sdk");

        fs::delete("$buildDir/.app/META-INF/manifest.mf");
        fs::delete("$buildDir/.app/META-INF/Manifest.mf");
        fs::delete("$buildDir/.app/META-INF/MANIFEST.MF");

        if (!$launcher['disable-launcher']) {
            Console::log("-> create jphp app launcher");

            Tasks::createDir("$buildDir/.app/JPHP-INF/");
            Tasks::createDir("$buildDir/.app/META-INF/");

            $includes = (array)$vendor->fetchPaths()['includes'];

            $includes = flow($includes, $event->package()->getIncludes())->toArray();

            if ($includes && !$launcher['bootstrap']) {
                $launcher['bootstrap'] = arr::pop($includes);
            }

            fs::formatAs("$buildDir/.app/JPHP-INF/launcher.conf", [
                'bootstrap.files' => flow($includes)->map(function ($one) {
                    return "res://$one";
                })->toString('|'),
                'bootstrap.file'  => $launcher['bootstrap'] ? "res://{$launcher['bootstrap']}" : 'res://JPHP-INF/.bootstrap.php',
            ], 'ini');

            Tasks::createFile("$buildDir/.app/META-INF/MANIFEST.MF");

            Stream::putContents("$buildDir/.app/META-INF/MANIFEST.MF", str::join([
                "Manifest-Version: 1.0",
                "Created-By: jppm (JPHP Packager " . $event->packager()->getVersion() . ")",
                "Main-Class: " . ($launcher['main-class'] ?? 'php.runtime.launcher.Launcher'),
                "",
            ], "\r\n"));
        } else {
            Console::log("-> jphp app launcher is disable.");
        }

        foreach ($metaInfServices as $name => $lines) {
            fs::ensureParent("$buildDir/.app/$name");
            Stream::putContents("$buildDir/.app/$name", str::join($lines, "\n"));
        }

        fs::scan("$buildDir/.app", function (File $file) use ($zip, $buildDir) {
            if ($file->isFile()) {
                $zip->addFile($file, fs::relativize($file, "$buildDir/.app"));
            }
        });

        $zip->close();

        Tasks::deleteFile("$buildDir/.app");

        foreach ($event->package()->getAny('app.assets', []) as $asset) {
            Tasks::deleteFile("$buildDir/" . fs::name($asset));

            if (fs::isDir($asset)) {
                Tasks::copy($asset, "$buildDir/" . fs::name($asset));
            } else {
                Tasks::copy($asset, "$buildDir/");
            }
        }

        switch (str::lower($event->package()->getAny('app.build.result'))) {
            case 'tar':
                Console::log("-> pack build dir to tar.gz archive ...");

                $archFile = File::createTemp($buildFileName, ".tar.gz");
                $archFile->deleteOnExit();

                $tar = new TarArchive(new GzipOutputStream($archFile));
                $tar->open();

                fs::scan("$buildDir/", function ($file) use ($buildDir, $tar) {
                    Console::log("-> pack '{0}'", fs::relativize($file, $buildDir . "/"));
                    if (fs::isFile($file)) {
                        $tar->addFile($file, fs::relativize($file, $buildDir . "/"));
                    } else {
                        $tar->addEmptyEntry(new TarArchiveEntry(fs::relativize($file, $buildDir . "/")));
                    }
                });

                $tar->close();

                Tasks::cleanDir("$buildDir/");
                Tasks::copy($archFile, "$buildDir/");
                fs::rename("$buildDir/" . fs::name($archFile), "{$buildFileName}.tar.gz");

                break;

            default:
                if (!$launcher['disable-launcher']) {
                    Console::log("\n   Use 'java -jar \"$buildDir/$buildFileName.jar\"' to run the result app.");
                }
        }

        $time = Time::millis() - $time;

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
     */
    function run(Event $event)
    {
        if (!$event->isFlag('light', 'l')) {
            Tasks::run('install');
        }

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

        $includes = flow($vendor->fetchPaths()['includes'], $event->package()->getIncludes())->toArray();

        if (!$launcher['bootstrap'] && $includes) {
            $launcher['bootstrap'] = arr::pop($includes);
        }

        if ($includes) {
            $sysArgs['bootstrap.files'] = flow($includes)->map(function ($one) {
                return "res://$one";
            })->toString('|');
        }

        if ($launcher['bootstrap']) {
            $sysArgs['bootstrap.file'] = 'res://' . $launcher['bootstrap'];
        }

        if (is_array($launcher['jvm-args'])) {
            $exec->setJvmArgs($launcher['jvm-args']);
        }

        $sysArgs['environment'] = 'dev';

        $exec->setSystemProperties($sysArgs);
        $exec->setMainClass($launcher['main-class'] ?: 'php.runtime.launcher.Launcher');

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