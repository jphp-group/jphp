<?php
namespace doc;

use packager\Annotations;
use packager\Package;
use packager\Packager;
use php\lib\str;
use phpx\parser\ClassRecord;

/**
 * Class DocIndex
 * @package doc
 */
class DocIndex
{
    /**
     * @var Package
     */
    private $package;

    /**
     * @var ClassRecord[]
     */
    private $classes = [];

    /**
     * @var string
     */
    private $lang;

    /**
     * @var array
     */
    private $langs;

    /**
     * @var string
     */
    private $defLang;

    /**
     * @var string
     */
    private $urlPrefix = 'api-docs/';

    /**
     * @var callable
     */
    private $translateFunc = null;

    /**
     * @var array
     */
    private $classImplements = [];

    /**
     * DocIndex constructor.
     * @param Package $package
     * @param array $langs
     * @param string $defLang
     * @param string $lang
     */
    public function __construct(Package $package, array $langs = [], string $defLang, string $lang)
    {
        $this->package = $package;
        $this->lang = $lang;
        $this->langs = $langs;
        $this->defLang = $defLang;
    }

    /**
     * @param callable $translateFunc
     */
    public function setTranslateFunc(callable $translateFunc)
    {
        $this->translateFunc = $translateFunc;
    }

    function addClass(ClassRecord $class)
    {
        $this->classes[$class->name] = $class;

        if ($class->parent) {
            $this->classImplements[$class->parent->name][$class->name] = $class;
        }
    }

    function hasClass(ClassRecord $class): bool
    {
        return isset($this->classes[$class->name]);
    }

    function getClass(string $name): ?ClassRecord
    {
        return $this->classes[$name];
    }

    /**
     * @param string $className
     * @return array
     */
    function getClassChildren(string $className): array
    {
        if ($r = $this->classImplements[$className]) {
            return $r;
        }

        return [];
    }

    function translate($message, ...$args)
    {
        if (is_array($message)) {
            return $message[$this->lang] ?? ($message[$this->defLang] ?? 'not translated');
        }

        if ($this->translateFunc) {
            return call_user_func_array($this->translateFunc, [$message, $args]);
        } else {
            return $message;
        }
    }

    /**
     * @param string $urlPrefix
     */
    public function setUrlPrefix(string $urlPrefix)
    {
        $this->urlPrefix = $urlPrefix;
    }

    /**
     * @param Packager $packager
     * @return string
     */
    function render(Packager $packager): string
    {
        $result = [];

        $line = [];

        foreach ($this->langs as $lang => $name) {
            $prefix = ".$lang";

            if ($lang === $this->defLang) {
                $prefix = "";
            }

            if ($this->lang === $lang) {
                $line[] = "**$name**";
            } else {
                $line[] = "[$name](README$prefix.md)";
            }
        }

        if (sizeof($this->langs) > 1) {
            $result[] = "#### " . str::join($line, " / ");
            $result[] = "";
            $result[] = "---";
            $result[] = "";
        }

        $result[] = "## {$this->package->getName()}";
        $result[] = "> {$this->translate('index.description', $this->package->getVersion(), $packager->getVersion())}";
        $result[] = "";

        if ($this->package->getDescription()) {
            $result[] = $this->package->getDescription();
        }

        $result[] = "";
        $result[] = "### {$this->translate('index.install.title')}";
        $result[] = "```";
        $result[] = "jppm add {$this->package->getNameWithVersion()}";
        $result[] = "```";
        $result[] = "";
        $result[] = "### {$this->translate('index.api.title')}";

        $result[] = "**{$this->translate('class.classes.title')}**";
        foreach ($this->getClassesByCategory() as $cat => $classes) {
            $result[] = "";
            $result[] = "#### `$cat`";
            $result[] = "";

            foreach ($classes as $class) {
                $line = "- [`{$class->shortName}`]({$this->classLink($class)})";

                $desc = str::lines(Annotations::getContent($class->comment, $this->lang), true)[0];

                if ($desc) {
                    $line .= "- _{$desc}_";
                }

                $result[] = $line;
            }
        }

        return str::join($result, "\n");
    }

    public function getClassesByCategory()
    {
        $result = [];

        foreach ($this->classes as $class) {
            $result[$class->namespace][$class->name] = $class;
        }

        return $result;
    }

    public function getLangPrefix()
    {
        $prefix = ".$this->lang";

        if ($this->lang === $this->defLang) {
            $prefix = "";
        }

        return $prefix;
    }

    public function classLink(ClassRecord $class)
    {
        return "{$this->urlPrefix}classes/" . str::replace($class->name, "\\", "/") . $this->getLangPrefix() . ".md";
    }

    public function functionLink(ClassRecord $function)
    {
        return "{$this->urlPrefix}functions/" . str::replace($function->name, "\\", "/") . $this->getLangPrefix() . ".md";
    }
}