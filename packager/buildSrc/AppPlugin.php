<?php

use app\AppPluginCompiler;
use app\AppPluginJavaRuntimeBuilder;
use app\AppPluginLaunch4JBuilder;
use compress\GzipOutputStream;
use compress\TarArchive;
use compress\TarArchiveEntry;
use compress\ZipArchive;
use compress\ZipArchiveEntry;
use packager\{cli\Console, Colors, Event, JavaExec, Package, Packager, Vendor};

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
        $launcherConf = $event->package()->getAny('app')['launcher'] ?: [];

        $bytecodeType = null;

        $defaultLauncherClass = 'php.runtime.launcher.Launcher';

        if ($event->isFlagExists('b', 'bytecode')) {
            $isBytecode = $event->isFlag('b', 'bytecode');
            if ($isBytecode) {
                $bytecodeType = 'phb';
            }
        } else {
            $isBytecode = (bool) $build['bytecode'];
            switch ($build['bytecode']) {
                case 'jvm':
                    $bytecodeType = 'jvm';
                    $defaultLauncherClass = 'php.runtime.launcher.StandaloneLauncher';
                    break;


                case 'phb':
                    $bytecodeType = 'phb';
                    break;

                default:
                    if ($build['bytecode'] === true) {
                        $bytecodeType = 'phb';
                    } else {
                        if (isset($build['bytecode']) && $build['bytecode'] !== false) {
                            Console::error("Unknown bytecode type ('app.build.bytecode' option): {0}", $build['bytecode']);
                            exit(-1);
                        }
                    }
            }
        }

        if ($isBytecode) {
            Console::info("[-b option] Bytecode {0} compilation is enabled!", Colors::withColor($bytecodeType, 'yellow'));
        }

        $exec = new JavaExec();

        $exec->addVendorClassPath($vendor);
        $exec->addPackageClassPath($event->package());

        $buildFileName = "{$event->package()->getName()}-{$event->package()->getVersion('last')}";

        if ($build['file-name']) {
            $buildFileName = $build['file-name'];
        }

        $buildType = $build['type'] ?? 'multi-jar';

        $buildDir = $event->package()->getConfigBuildPath();

        Tasks::createDir("$buildDir");
        Tasks::createFile("$buildDir/$buildFileName.jar");

        $zip = new ZipArchive("$buildDir/$buildFileName.jar");
        $zip->open();

        Tasks::createDir("$buildDir/.app");

        $metaInfServices = [];
        $jars = [];
        $vendorJars = [];

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
                $vendorJars[] = $classPath;

                switch ($buildType) {
                    case 'one-jar': {
                        Console::warn("app.build.type as 'one-jar' is unstable, try to use 'multi-jar'");

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

        if ($isBytecode) {
            Console::log("-> compile all 'php' sources to bytecode files");
            if ($build['bytecode'] === 'jvm') {
                $compiler = new AppPluginCompiler("$buildDir/.app", "$buildDir/.app");
            } else {
                $compiler = new AppPluginCompiler("$buildDir/.app");
            }

            $compiler->compile($event->package());
        }

        $launcherConf['types'] = $launcherConf['types'] ?? ['sh', 'bat'];

        if (!$launcherConf['disabled']) {
            Console::log("-> create jphp app launcher");

            Tasks::createDir("$buildDir/.app/JPHP-INF/");
            Tasks::createDir("$buildDir/.app/META-INF/");

            $includes = (array)$vendor->fetchPaths()['includes'];

            $includes = flow($includes, $event->package()->getIncludes());

            if ($bytecodeType === 'phb') { // replace php to phb is bytecode compiler
                $includes = $includes->map(function ($one) {
                    if (fs::ext($one) == "php") {
                        return str::replace(fs::pathNoExt($one), "\\", "/") . ".phb";
                    } else {
                        return $one;
                    }
                });

                if ($launcher['bootstrap'] && str::endsWith($launcher['bootstrap'], ".php")) {
                    $launcher['bootstrap'] = str::replace(fs::pathNoExt($launcher['bootstrap']), "\\", "/") . ".phb";
                }
            }

            $includes = $includes->toArray();

            if ($includes && !$launcher['bootstrap']) {
                $launcher['bootstrap'] = arr::pop($includes);
            }

            fs::formatAs("$buildDir/.app/JPHP-INF/launcher.conf", [
                'bootstrap.files' => flow($includes)->map(function ($one) {
                    return "res://$one";
                })->toString('|'),
                'bootstrap.file'  => $launcher['bootstrap'] ? "res://{$launcher['bootstrap']}" : ('res://JPHP-INF/.bootstrap.' . ($bytecodeType === 'phb' ? 'phb' : 'php')),
            ], 'ini');

            Tasks::createFile("$buildDir/.app/META-INF/MANIFEST.MF");

            Stream::putContents("$buildDir/.app/META-INF/MANIFEST.MF", str::join([
                "Manifest-Version: 1.0",
                "Created-By: jppm (JPHP Packager " . $event->packager()->getVersion() . ")",
                "Main-Class: " . ($launcher['main-class'] ?? $defaultLauncherClass),
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

        if (!$launcherConf['disabled'] && (arr::has($launcherConf['types'], "sh") || arr::has($launcherConf['types'], "bat"))) {
            $mainClass = $launcher['main-class'] ?: $defaultLauncherClass;

            $jars = ["$buildFileName.jar"];
            $libJars = flow(fs::scan("$buildDir/libs", ['extensions' => ['jar']], 1))
                ->map(function ($one) { return "libs/" . fs::name($one); })
                ->toArray();

            $templateArgs = [
                'APP_NAME' => $event->package()->getName(),
                'MAINCLASS' => $mainClass,
                'JAVA_OPTS' => flow($launcher['jvm-args'])->toString(" ")
            ];

            $templateArgs['JAVA_OPTS'] .= " -Dfile.encoding=" . ($launcher['encoding'] ?: 'UTF-8');

            $templateSh = fs::get("res://app/scripts/app");
            $templateBat = fs::get("res://app/scripts/app.bat");

            foreach ($templateArgs as $key => $templateArg) {
                $templateSh = str::replace($templateSh, "{{{$key}}}", $templateArg);
                $templateBat = str::replace($templateBat, "{{{$key}}}", $templateArg);
            }

            if (arr::has($launcherConf['types'], "sh")) {
                $templateSh = str::replace(
                    $templateSh,
                    '{{CLASSPATH}}',
                    flow($jars, $libJars)->map(function ($el) { return "\$APP_HOME/$el"; })->toString(":")
                );

                Tasks::createFile("$buildDir/$buildFileName", $templateSh, true);
            }

            if (arr::has($launcherConf['types'], "bat")) {
                $templateBat = str::replace(
                    $templateBat,
                    '{{CLASSPATH}}',
                    flow($jars, $libJars)->map(function ($el) { return "%APP_HOME%\\" . str::replace($el, "/", "\\"); })->toString(";")
                );

                Tasks::createFile("$buildDir/$buildFileName.bat", $templateBat, true);
            }

            if ($launcherConf['java']['embedded']) {
                Console::log("-> Build portable Java Runtime for App (via 'jlink')");
                $javaRuntimeBuilder = new AppPluginJavaRuntimeBuilder();

                if ($launcherConf['java']['jdk']) {
                    if (is_string($launcherConf['java']['jdk'])) {
                        $javaRuntimeBuilder->setJavaHomeEmbedded($launcherConf['java']['jdk']);
                    } else if (is_array($launcherConf['java']['jdk'])) {
                        $javaRuntimeBuilder->setJavaHomeEmbedded($launcherConf['java']['jdk'][Package::getOS()]);
                    }
                }

                foreach ($vendorJars as $jar) {
                    $javaRuntimeBuilder->addJar($jar);
                }

                $tree = $event->packager()->fetchDependencyTree($event->package(), '');
                $tree->eachDep(function (Package $pkg) use ($javaRuntimeBuilder) {
                    $javaRuntimeBuilder->addJvmModules(...$pkg->getAny('jvm-modules', []));
                });

                $javaRuntimeBuilder->build($buildDir);
                Console::log("-> Java Runtime is build successfully, the app will use the 'jre' dir as JAVA_HOME");
            }

            /*
             * TODO Implement this, now launch4j doesn't support Java 11+
             * if (arr::has($launcherConf['types'], "exe-gui")) {
                $launcher4j = new AppPluginLaunch4JBuilder($event->package(), $buildDir, $launcherConf, $mainClass, 'gui');
                $launcher4j->build();
            } else if (arr::has($launcherConf['types'], "exe-console")) {
                $launcher4j = new AppPluginLaunch4JBuilder($event->package(), $buildDir, $launcherConf, $mainClass, 'console');
                $launcher4j->build();
            }*/
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
                if (!$launcher['disable-launcher'] && !$launcherConf['disabled']) {
                    if (str::posIgnoreCase(System::osName(), 'win') === -1) {
                        if (arr::has($launcherConf['types'], 'sh')) {
                            Console::log("\n   Use '{0}' to run the result app.", fs::normalize("./$buildDir/$buildFileName"));
                        } else {
                            Console::warn("\n   Launcher script is not available, app.launcher.types hasn't 'sh' value");
                        }
                    } else {
                        if (arr::has($launcherConf['types'], 'bat')) {
                            Console::log("\n   Use '{0}.bat' to run the result app.", fs::normalize($buildDir . "\\" . $buildFileName));
                        } else {
                            Console::warn("\n   Launcher script is not available, app.launcher.types hasn't 'bat' value");
                        }
                    }
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
     * @throws \php\lang\IllegalArgumentException
     * @throws \php\lang\IllegalStateException
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