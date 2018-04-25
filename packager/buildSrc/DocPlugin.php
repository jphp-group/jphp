<?php

use doc\DocClass;
use doc\DocIndex;
use packager\cli\Console;
use packager\Event;
use packager\Vendor;
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

    public function __construct(Event $event)
    {
        $this->langs = $event->package()->getAny('doc.langs', ['en' => 'English']);
        $this->urlPrefix = $event->package()->getAny('doc.url-prefix', 'api-docs/');
        $this->dir = $event->package()->getAny('doc.dir', './api-docs');
        $this->defLang = arr::firstKey($this->langs);
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
     * @jppm-description build html php doc
     * @param Event $event
     */
    public function build(Event $event)
    {
        $env = new Environment(null, Environment::HOT_RELOAD);

        $sources = $event->package()->getSources() + ["./sdk"];

        $docDir = $this->dir;

        foreach ($this->langs as $lang => $langName) {
            $suffix = $this->defLang == $lang ? "" : ".$lang";

            $docIndex = new DocIndex($event->package(), $this->langs, $this->defLang, $lang);
            $docIndex->setUrlPrefix($this->urlPrefix);

            foreach ($sources as $source) {
                $originSource = $source;
                $source = "./$source";

                fs::scan($source, ['extensions' => ['php', 'phb'], 'callback' => function ($filename)
                                use ($env, $docIndex, $event, $source, $originSource, $docDir, $suffix, $lang) {
                    $uniqueId = fs::relativize($filename, $source);

                    $sourceFile = $this->parseFile($filename, $uniqueId);
                    $classes = $sourceFile->moduleRecord->getClasses();

                    flow($classes)->each(function (ClassRecord $cls) use ($docIndex, $event, $docDir) {
                        $docIndex->addClass($cls);
                    });

                    flow($classes)->each(function (ClassRecord $cls) use ($docDir, $docIndex, $suffix, $lang, $uniqueId, $originSource) {
                        $docClass = new DocClass($docIndex, $cls, $lang);
                        $docClass->setFile("$originSource/$uniqueId");
                        $docClass->setSrcFile($uniqueId);

                        $filename = "$docDir/classes/" . str::replace($cls->name, "\\", "/");

                        Tasks::createFile("{$filename}{$suffix}.md", $docClass->render());
                    });
                }]);
            }

            Tasks::createFile("{$this->dir}/README$suffix.md", $docIndex->render($event->packager()));
        };
    }
}