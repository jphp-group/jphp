<?php

namespace packager;

use php\io\IOException;
use php\io\Stream;
use php\lib\fs;
use php\lib\str;

/**
 * Class Ignore
 * @package packager
 */
class Ignore
{
    /**
     * @var array
     */
    private $rules = [];

    /**
     * @var string
     */
    private $base = '';

    /**
     * Ignore constructor.
     * @param array $rules
     */
    public function __construct(array $rules)
    {
        $this->rules = $rules;
    }

    /**
     * @param string $dir
     * @param string $prefix
     * @param int $depth
     * @return Ignore
     */
    public static function ofDir(string $dir, string $prefix = '', int $depth = 0): Ignore
    {
        $ignore = Ignore::ofFile("$dir/.jppmignore", $prefix);

        /*if ($depth < 3) {
            $dirs = fs::scan($dir, ['excludeFiles' => true]);

            foreach ($dirs as $sub) {
                $name = fs::name($sub);

                $subIgnore = Ignore::ofDir("$dir/$name", $prefix ? "$prefix/$name/" : '', $depth + 1);
                if ($subIgnore) {
                    $ignore->addIgnore($subIgnore);
                }
            }
        }*/

        return $ignore;
    }

    /**
     * @param string $file
     * @param string $prefix
     * @return Ignore
     */
    public static function ofFile(string $file, string $prefix = ''): Ignore
    {
        $rules = [];

        try {
            $stream = Stream::of($file);
            $stream->eachLine(function ($line) use (&$rules, $prefix) {
                $line = str::trim($line);

                if ($line[0] === '#') return;

                $rules[] = $prefix . $line;
            });
            $stream->close();
        } catch (IOException $e) {
            return new Ignore([]);
        }

        return new Ignore($rules);
    }

    /**
     * @param string $rule
     * @return $this
     */
    public function addRule(string $rule)
    {
        $this->rules[] = $rule;
        return $this;
    }

    /**
     * @param Ignore $ignore
     */
    public function addIgnore(Ignore $ignore)
    {
        $this->rules = flow($this->rules, $ignore->rules)->toArray();
    }

    /**
     * @param string $path
     * @return bool
     */
    public function test(string $path)
    {
        foreach ($this->rules as $rule) {
            if (fs::match($this->base . $path, 'glob:' . $rule)) {
                return true;
            }
        }

        return false;
    }

    public function setBase($string)
    {
        $this->base = $string;
        return $this;
    }
}