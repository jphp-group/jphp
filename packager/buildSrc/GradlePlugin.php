<?php

use packager\cli\Console;
use packager\Event;
use packager\Package;
use packager\Vendor;
use php\lang\Process;
use php\lang\System;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use text\TextWord;

/**
 * Class GradlePlugin
 *
 * @jppm-task-prefix gradle
 *
 * @jppm-task install
 * @jppm-task build
 * @jppm-task init
 */
class GradlePlugin
{
    /**
     * @var array|mixed|null
     */
    private $config = [];

    public function __construct(Event $event)
    {
        if ($event->package()) {
            $this->config = $event->package()->getAny('gradle', []);
        }
    }

    protected function makeDepSection(array $deps, string $gradleMethod = 'compile'): array
    {
        $compile = [];

        foreach ($deps as $dep) {
            if (is_array($dep)) {
                $exclude = flow($dep['exclude'])->map(function ($s) {
                    [$group, $module] = str::split($s, ":", 2);
                    return "  exclude(group: '$group', module: '$module')";
                })->toString("\n");

                $compile[] = "$gradleMethod ('{$dep['dep']}') { \n$exclude }";
            } else {
                if (str::trim($dep)) {
                    if (str::startsWith($dep, 'file:')) {
                        $dep = str::sub($dep, 5);
                        $compile[] = "  $gradleMethod files('$dep')";
                    } else {
                        $compile[] = "  $gradleMethod '$dep'";
                    }
                }
            }
        }

        return $compile;
    }

    protected function makeGradleBuild(Package $pkg)
    {
        $vendor = new Vendor($pkg->getConfigVendorPath());

        $paths = $vendor->fetchPaths();

        $deps = [];
        foreach ((array) $paths['classPaths'][''] as $file) {
            if (fs::ext($file) === 'jar') {
                $file = $vendor->getDir() . $file;

                if (str::startsWith($file, './')) {
                    $file = str::sub($file, 2);
                    $deps[] = $file;
                }
            }
        }

        $compile = $this->makeDepSection((array) $this->config['deps'], 'compile');
        $implementation = $this->makeDepSection((array) $this->config['implDeps'], 'implementation');
        $provided = $this->makeDepSection((array) $this->config['providedDeps'], 'provided');

        $jars = fs::scan("./jars/", ['extensions' => ['jar'], 'excludeDirs' => true], 1);

        foreach ($jars as $jar) {
            $jar = fs::name($jar);
            $provided[] = "  provided files('jars/{$jar}')";
        }

        if ($deps) {
            $provided[] = "  provided files('" . str::join($deps, "',\r\n '") . "')";
        }

        $addRepos = [];
        if ($this->config['repos']) {
            $addRepos = flow($this->config['repos'])->map(function ($repo) {
                return "  maven { url '$repo' }";
            })->toArray();
        }

        $lines = [
            'apply plugin: "java"',
            'apply plugin: "idea"',
            'apply plugin: "eclipse"',
            "",
            "version '{$pkg->getVersion()}'",
            "",
            "compileJava {",
            "    sourceCompatibility = '1.8'",
            "    targetCompatibility = '1.8'",
            "}",
            "",
            "sourceSets {",
            "    main.java.srcDirs = ['src-jvm/main/java']",
            "    main.resources.srcDirs = ['src-jvm/main/resources']",
            "}",
            "",
            "repositories {",
            "  mavenLocal()",
            "  mavenCentral()",
            "  jcenter()",
            "  maven { url 'https://oss.sonatype.org/content/groups/public' }",
            $addRepos,
            "}",
            "",
            "configurations { provided }",
            "",
            "sourceSets { main { compileClasspath += configurations.provided } }",
            "",
            "dependencies {",
                str::join($provided, "\r\n"),
                str::join($implementation, "\r\n"),
                str::join($compile, "\r\n"),
            "}",
            "",
            "jar {",
            "   destinationDir = file('jars/')",
            "   baseName = 'x-{$pkg->getName()}'",
            "}",
            "",
            "task copyJars(type: Copy) {",
            "  from project.configurations.compile",
            "  into file(projectDir.path + '/jars/')",
            "  rename { name -> 'x-' + name }",
            "}"
        ];

        Tasks::createFile("./build.gradle", str::join(arr::flatten($lines), "\r\n"));
    }

    /**
     * @param $commands
     * @return Process
     */
    public function gradleProcess(array $commands): Process
    {
        if (str::contains(str::lower(System::osName()), 'windows')) {
            $args = ['cmd', '/c', 'gradlew.bat'];
        } else {
            $args = ['sh', 'gradlew'];
        }

        $args = flow($args, $commands, ['--stacktrace'])->toArray();

        return new Process($args, fs::abs('./'), [
            'GRADLE_OPTS' => '-Dorg.gradle.daemon=false -Dfile.encoding=UTF-8'
        ]);
    }

    /**
     * @jppm-description Install gradle build files.
     * @jppm-dependency-of install
     * @jppm-need-package
     * @param Event $event
     */
    public function install(Event $event)
    {
        Tasks::run('install', [], 'gradle');

        $this->makeGradleBuild($event->package());

        Tasks::createDir('./gradle/wrapper');
        Tasks::createFile('./gradlew', str::replace(fs::get('res://gradle/gradlew'), "\r\n", "\n"));
        Tasks::createFile('./gradlew.bat', fs::get('res://gradle/gradlew.bat'));

        (new \php\io\File('./gradlew'))->setExecutable(true);

        fs::copy('res://gradle/wrapper/gradle-wrapper.jar', './gradle/wrapper/gradle-wrapper.jar');
        fs::copy('res://gradle/wrapper/gradle-wrapper.properties', './gradle/wrapper/gradle-wrapper.properties');
    }

    /**
     * @jppm-need-package
     * @jppm-depends-on gradle:install
     * @jppm-dependency-of publish
     * @jppm-dependency-of build
     * @param Event $event
     */
    public function build(Event $event)
    {
        fs::clean('./jars/', [
            'namePattern' => '^x\\-.*',
            'excludeDirs' => true
        ]);

        $gradle = $this->gradleProcess(['copyJars', 'jar'])->inheritIO()->startAndWait();

        if ($gradle->getExitValue() !== 0) {
            Console::error("Failed to gradle:install.");
            exit(-1);
        }
    }

    /**
     * @jppm-need-package
     * @jppm-depends-on gradle:install
     * @param Event $event
     */
    public function init(Event $event)
    {
        Tasks::createDir('src-jvm/main/java');
        Tasks::createDir('src-jvm/main/resources');

        $name = $event->package()->getName();
        $name = (new TextWord($name))->capitalizeFully('-_ ');

        $name = str::replace($name, '-', '');
        $name = str::replace($name, '_', '');
        $name = str::replace($name, ' ', '');

        $package = Console::read('Enter package name of java sources:', 'php.pkg.' . str::lower($name));
        $packageDir = str::replace($package, '.', '/');
        $ns = str::lower($name);

        Tasks::createDir("src-jvm/main/java/$packageDir");
        Tasks::createDir("src-jvm/main/java/$packageDir/classes");

        $template = <<<DOC
package $package;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class {$name}Extension extends Extension {
    public static final String NS = "$ns";
    
    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }
    
    @Override
    public void onRegister(CompileScope scope) {
        // register classes ...
    }
}
DOC;

        Tasks::createFile("src-jvm/main/java/$packageDir/{$name}Extension.java", $template);
        Tasks::createFile("src-jvm/main/resources/META-INF/services/php.runtime.ext.support.Extension", "{$package}.{$name}Extension");

        Console::log("Success, java project has been created.");
        Console::log("Done.");
    }
}