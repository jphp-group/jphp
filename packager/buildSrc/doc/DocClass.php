<?php

namespace doc;


use packager\Annotations;
use packager\Package;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use phpx\parser\ClassRecord;
use phpx\parser\MethodRecord;
use phpx\parser\PropertyRecord;

class DocClass
{
    /**
     * @var ClassRecord
     */
    private $class;

    /**
     * @var DocIndex
     */
    private $index;

    /**
     * @var string
     */
    private $lang;

    /**
     * @var string
     */
    private $file;

    /**
     * @var string
     */
    private $srcFile;

    /**
     * DocClass constructor.
     * @param DocIndex $index
     * @param ClassRecord $class
     * @param string $lang
     * @internal param Package $package
     */
    public function __construct(DocIndex $index, ClassRecord $class, string $lang = 'def')
    {
        $this->class = $class;
        $this->index = $index;
        $this->lang = $lang;
    }

    /**
     * @param string $file
     */
    public function setFile(string $file)
    {
        $this->file = $file;
    }

    /**
     * @param string $srcFile
     */
    public function setSrcFile(string $srcFile)
    {
        $this->srcFile = $srcFile;
    }

    /**
     * @param string $lang
     */
    public function setLang(string $lang)
    {
        $this->lang = $lang;
    }

    public function render(): string
    {
        $class = $this->class;

        $result = [
            "# {$class->shortName}",
            ""
        ];

        $line = "- **class** `{$class->shortName}` (`$class->name`)";
        if ($class->parent) {
            if ($this->index->hasClass($class->parent)) {
                $line .= " **extends** [`{$class->parent->shortName}`]({$this->index->classLink($class->parent)})";
            } else {
                $line .= " **extends** `{$class->parent->shortName}` (`{$class->parent->name}`)";
            }
        }

        $result[] = $line;

        $packages = str::split(Annotations::get("packages", $class->comment), ',');
        $packages = flow($packages)->map([str::class, 'trim'])->toArray();

        if ($packages) {
            $result[] = "- **package** `" . arr::first($packages) . "`";
        }

        if ($this->srcFile) {
            if ($this->file) {
                $result[] = "- **source** [`$this->srcFile`]($this->file)";
            } else {
                $result[] = "- **source** `$this->srcFile`";
            }
        }

        $result[] = "";

        $description = Annotations::getContent($class->comment, $this->lang);

        if ($description !== "") {
            $result[] = "**Description**";
            $result[] = "";
            $result[] = $description;
        }

        $properties = $class->getProperties();
        if ($properties) {
            $result[] = "";
            $result[] = "---";
            $result[] = "";
            $result[] = "#### Properties";
            $result[] = "";

            foreach ($properties as $property) {
                $result[] = $this->renderPropertyLine($property);
            }
        }

        $staticMethods = flow($class->getMethods())->find(function ($m) { return $m->static; })->toArray();

        if ($staticMethods) {
            $result[] = "";
            $result[] = "---";
            $result[] = "";
            $result[] = "#### Static Methods";
            $result[] = "";

            foreach ($staticMethods as $method) {
                $result[] = $this->renderMethodLine($method);
            }
        }

        $methods = flow($class->getMethods())->find(function ($m) { return !$m->static; })->toArray();

        if ($methods) {
            $result[] = "";
            $result[] = "---";
            $result[] = "";
            $result[] = "#### Methods";
            $result[] = "";


            foreach ($methods as $method) {
                $result[] = $this->renderMethodLine($method);
            }
        }

        return str::join($result, "\n");
    }

    public function renderPropertyLine(PropertyRecord $property): string
    {
        $anchor = "#prop-" . str::lower($property->name);

        $type = Annotations::get('var', $property->comment, 'mixed');

        $line = "- `->`[`{$property->name}`]({$anchor}) : `$type`";
        $desc = Annotations::getContent($property->comment, $this->lang);

        if ($desc) {
            $line .= " - _{$desc}_";
        }

        return $line;
    }

    public function renderMethodLine(MethodRecord $method): string
    {
        $anchor = "#method-" . str::lower($method->name);

        $type = Annotations::get('return', $method->comment, 'void');
        $deprecated = Annotations::get('deprecated', $method->comment);

        $line = "- ";

        if ($method->static) {
            $line .= "`{$method->classRecord->shortName} ::`";
        } else {
            $line .= "`->`";
        }

        $line .= "[`{$method->name}()`]({$anchor})";

        if ($deprecated) {
            $line .= " **deprecated**";
        }

        $desc = Annotations::getContent($method->comment, $this->lang);

        if ($desc) {
            $line .= " - _{$desc}_";
        }

        return $line;
    }
}