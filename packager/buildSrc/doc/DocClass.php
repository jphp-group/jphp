<?php

namespace doc;


use packager\Annotations;
use packager\Package;
use php\lib\arr;
use php\lib\fs;
use php\lib\str;
use phpx\parser\ArgumentRecord;
use phpx\parser\ClassRecord;
use phpx\parser\MethodRecord;
use phpx\parser\PropertyRecord;
use phpx\parser\SourceFile;

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
     * @var array
     */
    private $excludeMethods = [];
    /**
     * @var SourceFile
     */
    private $sourceFile;

    /**
     * @var array
     */
    private $extra = [];

    /**
     * DocClass constructor.
     * @param DocIndex $index
     * @param ClassRecord $class
     * @param string $lang
     * @param SourceFile $sourceFile
     * @internal param Package $package
     */
    public function __construct(DocIndex $index, ClassRecord $class, string $lang = 'def', SourceFile $sourceFile = null)
    {
        $this->class = $class;
        $this->index = $index;
        $this->lang = $lang;
        $this->sourceFile = $sourceFile;
    }

    /**
     * @param array $extra
     */
    public function setExtra(array $extra)
    {
        $this->extra = $extra;
    }

    /**
     * @param array $excludeMethods
     */
    public function setExcludeMethods(array $excludeMethods)
    {
        $this->excludeMethods = $excludeMethods;
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

        $line = "- **{$this->index->translate('common.class')}** `{$class->shortName}` (`$class->name`)";
        if ($class->parent) {
            if ($this->index->hasClass($class->parent)) {
                $line .= " **{$this->index->translate('common.extends')}** [`{$class->parent->shortName}`]({$this->index->classLink($class->parent)})";
            } else {
                $line .= " **{$this->index->translate('common.extends')}** `{$class->parent->shortName}` (`{$class->parent->name}`)";
            }
        }

        $result[] = $line;

        $packages = str::split(Annotations::get("packages", $class->comment), ',');
        $packages = flow($packages)->map([str::class, 'trim'])->toArray();

        if ($packages) {
            $result[] = "- **{$this->index->translate('common.package')}** `" . arr::first($packages) . "`";
        }

        if ($this->srcFile) {
            $result[] = "- **{$this->index->translate('common.source')}** `$this->srcFile`";
        }

        if ($children = $this->index->getClassChildren($this->class->name)) {
            $result[] = "";
            $result[] = "**{$this->index->translate('class.children.list')}**";
            $result[] = "";

            $result[] = "> " . flow($children)->map(function (ClassRecord $cls) {
                $r = "[$cls->shortName]({$this->index->classLink($cls)})";
                return $r;
            })->toString(', ');
        }

        $description = Annotations::getContent($class->comment, $this->lang);

        if ($description !== "") {
            $result[] = "";
            $result[] = "**{$this->index->translate('class.description.title')}**";
            $result[] = "";
            $result[] = $description;
        }

        $properties = $class->getProperties();
        if ($properties) {
            $result[] = "";
            $result[] = "---";
            $result[] = "";
            $result[] = "#### {$this->index->translate('class.properties.title')}";
            $result[] = "";

            foreach ($properties as $property) {
                $result[] = $this->renderPropertyLine($property);
            }

            if ($class->parent) {
                if ($this->index->hasClass($class->parent)) {
                    $result[] = "- *{$this->index->translate('class.see-parent-class')}* [{$class->parent->shortName}]({$this->index->classLink($class->parent)}).";
                }
            }
        }

        $staticMethods = flow($class->getMethods())
            ->find(function ($m) { return $m->static; })
            ->find(function ($m) { return !arr::has($this->excludeMethods, $m->name); })
            ->toArray();

        if ($staticMethods) {
            $result[] = "";
            $result[] = "---";
            $result[] = "";
            $result[] = "#### {$this->index->translate('class.static-method.title')}";
            $result[] = "";

            foreach ($staticMethods as $method) {
                $result[] = $this->renderMethodLine($method);
            }

            if ($class->parent) {
                if ($this->index->hasClass($class->parent)) {
                    $result[] = "- {$this->index->translate('class.see-parent-class')} [{$class->parent->shortName}]({$this->index->classLink($class->parent)})";
                }
            }
        }

        $methods = flow($class->getMethods())
            ->find(function ($m) { return !$m->static; })
            ->find(function ($m) { return !arr::has($this->excludeMethods, $m->name); })
            ->toArray();

        if ($methods) {
            $result[] = "";
            $result[] = "---";
            $result[] = "";
            $result[] = "#### {$this->index->translate('class.method.title')}";
            $result[] = "";


            foreach ($methods as $method) {
                $result[] = $this->renderMethodLine($method);
            }

            if ($class->parent) {
                if ($this->index->hasClass($class->parent)) {
                    $result[] = "- {$this->index->translate('class.see-parent-class')} [{$class->parent->shortName}]({$this->index->classLink($class->parent)})";
                }
            }
        }

        if ($staticMethods) {
            $result[] = "";
            $result[] = "---";
            $result[] = "# {$this->index->translate('class.static-method.title')}";
            $result[] = "";

            foreach ($staticMethods as $i => $method) {
                arr::push($result, ...$this->renderMethod($method));

                if ($i !== sizeof($staticMethods) - 1) {
                    $result[] = "";
                    $result[] = "---";
                    $result[] = "";
                }
            }
        }

        if ($methods) {
            $result[] = "";
            $result[] = "---";
            $result[] = "# {$this->index->translate('class.method.title')}";
            $result[] = "";

            foreach ($methods as $i => $method) {
                arr::push($result, ...$this->renderMethod($method));

                if ($i !== sizeof($methods) - 1) {
                    $result[] = "";
                    $result[] = "---";
                    $result[] = "";
                }
            }
        }

        return str::join($result, "\n");
    }

    public function renderTypeHint(string $type): string
    {
        if ($cls = $this->index->getClass($type)) {
            $type = "[`$cls->shortName`]({$this->index->classLink($cls)})";
        } else if ($this->sourceFile) {
            $cls = $this->index->getClass($this->sourceFile->fetchFullName($type, $this->class->namespaceRecord, 'CLASS'));

            if ($cls) {
                $type = "[`$cls->shortName`]({$this->index->classLink($cls)})";
            } else {
                $type = "`$type`";
            }
        } else {
            $type = "`$type`";
        }

        return $type;
    }

    public function renderPropertyLine(PropertyRecord $property): string
    {
        $anchor = "#prop-" . str::lower($property->name);

        $type = Annotations::get('var', $property->comment, 'mixed');


        $line = "- `->`[`{$property->name}`]({$anchor}) : {$this->renderTypeHint($type)}";
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
            $line .= " **{$this->index->translate('common.deprecated')}**";
        }

        $desc = str::lines(Annotations::getContent($method->comment, $this->lang), true)[0];

        if ($desc) {
            $line .= " - _{$desc}_";
        }

        return $line;
    }

    public function renderMethod(MethodRecord $method): array
    {
        $result = [];

        $anchor = "method-" . str::lower($method->name);

        $result[] = "<a name=\"$anchor\"></a>";
        $result[] = "";
        $result[] = "### {$method->name}()";
        $result[] = "```php";

        $optionalExists = false;

        $_paramsAnn = Annotations::get('param', $method->comment, []);

        $paramsAnn = [];

        foreach ($_paramsAnn as $one) {
            [$prmType, $prmName, $prmDesc] = str::split($one, ' ', 3);

            $prmType = str::trim($prmType);
            $prmName = str::trim($prmName);

            if ($prmName[0] === '$') $prmName = str::sub($prmName, 1);

            $paramsAnn[$prmName] = $prmType;
        }

        $args = flow($method->argumentRecords)->map(function (ArgumentRecord $arg, $i) use (&$optionalExists, $method, $paramsAnn) {
            $prefix = "mixed";

            if ($arg->hintTypeClass) {
                $prefix = "$arg->hintTypeClass";
            } else if ($paramsAnn[$arg->name]) {
                $prefix = $paramsAnn[$arg->name];
            } else if ($arg->hintType !== 'ANY') {
                $prefix = str::lower($arg->hintType);
            }

            if (!$prefix) $prefix = 'mixed';

            $result = "$prefix \${$arg->name}";

            if ($arg->optional && !$optionalExists) {
                $result = "[ $result";
                $optionalExists = true;
            } elseif ($arg->optional && $i == sizeof($method->argumentRecords) - 1) {
                $result = "$result ]";
            }

            return $result;
        })->toString(", ");


        [$returnAnnType, $returnAnnDesc] = str::split(Annotations::get('return', $method->comment), ' ', 2);

        $return = "void";

        if ($method->returnTypeHintClass) {
            $return = "$method->returnTypeHintClass";
        } else if ($returnAnnType) {
            $return = str::trim($returnAnnType);
        } elseif ($method->returnTypeHint) {
            $return = str::lower($method->returnTypeHint);
        }

        if ($method->static) {
            $result[] = "{$method->classRecord->shortName}::{$method->name}($args): $return";
        } else {
            $result[] = "{$method->name}($args): $return";
        }

        $result[] = "```";

        $extra = $this->extra['methods'][$method->name];

        if ($extra['short-desc']) {
            $result[] = flow(str::lines($this->index->translate($extra['short-desc'])))
                ->map(function ($l) { return "> $l"; })
                ->toString("\n");
            $result[] = "";
        }

        $desc = Annotations::getContent($method->comment, $this->lang);
        if ($desc && !$extra['desc']) {
            $result[] = "$desc";
        }

        if ($extra['desc']) {
            $result[] = "";
            $result[] = $this->index->translate($extra['desc']);
        }

        if (is_array($extra['eg'])) {
            $result[] = "";
            $result[] = "_Examples:_";
            $result[] = "";

            foreach ($extra['eg'] as $example) {
                $result[] = "- **{$this->index->translate($example['title'])}**";
                $result[] = "";
                $result[] = $example['source'];
            }
        }

        return $result;
    }
}