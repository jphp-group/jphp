<?php

use doc\DocClass;
use doc\DocIndex;
use packager\cli\Console;
use packager\Event;
use packager\Vendor;
use php\io\IOException;
use php\lang\Environment;
use php\lang\Module;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use phpx\parser\ClassRecord;
use phpx\parser\SourceFile;
use phpx\parser\SourceManager;
use phpx\parser\SourceTokenizer;

/**
 * Class PhpDocPlugin
 *
 * @jppm-task-prefix doc
 *
 * @jppm-task build
 * @jppm-task clean
 */
class DocPlugin
{
    /**
     * @var array
     */
    private $langs;

    private $defLang;

    private $urlPrefix;

    private $dir;
    private $sourceFiles = [];

    /**
     * @var array
     */
    private $excludeClasses = [];
    private $excludeFunctions = [];
    private $excludeMethods = [];

    /**
     * @var array
     */
    private $messages = [];

    /**
     * @var array
     */
    private $stubDirs = [];
    private $extraDir;

    public function __construct(Event $event)
    {
        if ($event->package()) {
            $this->langs = $event->package()->getAny('doc.langs', ['en' => 'English']);
            $this->urlPrefix = $event->package()->getAny('doc.url-prefix', '');
            $this->dir = $event->package()->getAny('doc.dir', './api-docs');
            $this->extraDir = $event->package()->getAny('doc.extra-dir', './api-docs-extra');

            $this->stubDirs = $event->package()->getAny('doc.stub-dirs', ['./sdk']);

            $this->excludeClasses = $event->package()->getAny('doc.exclude-classes', []);
            $this->excludeMethods = $event->package()->getAny('doc.exclude-methods', []);
            $this->excludeFunctions = $event->package()->getAny('doc.exclude-functions', []);

            $this->defLang = arr::firstKey($this->langs);

            foreach ($this->langs as $lang => $name) {
                try {
                    $this->messages[$lang] = fs::parse("res://doc/messages/$lang.yml");
                } catch (IOException $e) {
                    continue;
                }
            }
        }
    }

    protected function parseFile($filename, string $uniqueId): SourceFile
    {
        if ($source = $this->sourceFiles[fs::abs($filename)]) {
            return $source;
        }

        Console::log("-> parse doc '{0}'", $filename);

        $source = new SourceFile($filename, $uniqueId);
        $source->update(new SourceManager());
        $this->sourceFiles[fs::abs($filename)] = $source;

        return $source;
    }

    /**
     * @jppm-need-package true
     * @jppm-description Remove build API documentation files!
     * @param Event $event
     */
    public function clean(Event $event)
    {
        $docDir = $this->dir;

        Tasks::cleanDir($docDir);
    }

    /**
     * @jppm-depends-on doc:clean
     * @jppm-need-package true
     * @jppm-description Build markdown API documentation.
     * @param Event $event
     */
    public function build(Event $event)
    {
        $sources = flow($event->package()->getSources(), $this->stubDirs)->toArray();

        $docDir = $this->dir;

        foreach ($this->langs as $lang => $langName) {
            $suffix = $this->defLang == $lang ? "" : ".$lang";

            $docIndex = new DocIndex($event->package(), $this->langs, $this->defLang, $lang);
            $docIndex->setUrlPrefix($this->urlPrefix);
            $docIndex->setTranslateFunc(function ($message, array $args = []) use ($lang) {
                $msg = $this->messages[$lang][$message];

                if (!$msg && $lang !== $this->defLang) {
                    $msg = $this->messages[$this->defLang][$message];
                }

                if (!$msg) {
                    return $message;
                }

                foreach ($args as $i => $arg) {
                    $msg = str::replace($msg, "\{$i\}", $arg);
                }

                return $msg;
            });

            foreach ($sources as $source) {
                $originSource = $source;
                $source = "./$source";

                fs::scan($source, ['extensions' => ['php'], 'callback' => function ($filename)
                        use ($docIndex, $event, $source, $originSource, $docDir, $suffix, $lang) {
                    $uniqueId = fs::relativize($filename, $source);

                    $sourceFile = $this->parseFile($filename, $uniqueId);

                    $classes = $sourceFile->moduleRecord->getClasses();

                    flow($classes)->each(function (ClassRecord $cls) use ($docIndex, $event, $docDir) {
                        if (!arr::has($this->excludeClasses, $cls->name)) {
                            $docIndex->addClass($cls);
                        }
                    });
                }]);

                fs::scan($source, ['extensions' => ['php'], 'callback' => function ($filename)
                use ($docIndex, $event, $source, $originSource, $docDir, $suffix, $lang) {
                    $uniqueId = fs::relativize($filename, $source);

                    $sourceFile = $this->parseFile($filename, $uniqueId);

                    if (fs::isFile($extraFile = "$this->extraDir/" . fs::pathNoExt($uniqueId) . ".yml")) {
                        $extra = fs::parse($extraFile);
                    } else {
                        $extra = [];
                    }

                    $classes = $sourceFile->moduleRecord->getClasses();

                    flow($classes)->each(function (ClassRecord $cls)
                    use ($docDir, $docIndex, $suffix, $lang, $uniqueId, $originSource, $sourceFile, $extra) {
                        if (arr::has($this->excludeClasses, $cls->name)) return;

                        $docClass = new DocClass($docIndex, $cls, $lang, $sourceFile);
                        $docClass->setFile("$originSource/$uniqueId");
                        $docClass->setSrcFile($uniqueId);
                        $docClass->setExcludeMethods($this->excludeMethods);
                        $docClass->setExtra($extra);

                        $filename = "$docDir/classes/" . str::replace($cls->name, "\\", "/");

                        Tasks::createFile("{$filename}{$suffix}.md", $docClass->render());
                    });
                }]);
            }

            Tasks::createFile("{$this->dir}/README$suffix.md", $docIndex->render($event->packager()));
        };
    }
}