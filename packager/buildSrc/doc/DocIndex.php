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

    function addClass(ClassRecord $class)
    {
        $this->classes[$class->name] = $class;
    }

    function hasClass(ClassRecord $class): bool
    {
        return isset($this->classes[$class->name]);
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
        $result[] = "> version {$this->package->getVersion()}, created by JPPM v" . $packager->getVersion();

        if ($this->package->getDescription()) {
            $result[] = $this->package->getDescription();
        }

        $result[] = "";
        $result[] = "### Install";
        $result[] = "```";
        $result[] = "jppm add {$this->package->getNameWithVersion()}";
        $result[] = "```";
        $result[] = "";
        $result[] = "### API";

        $result[] = "**Classes**";
        foreach ($this->classes as $class) {
            $line = "- [`{$class->name}`]({$this->classLink($class)})";

            $desc = Annotations::getContent($class->comment, $this->lang);

            if ($desc) {
                $line .= "- _{$desc}_";
            }

            $result[] = $line;
        }

        return str::join($result, "\n");
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