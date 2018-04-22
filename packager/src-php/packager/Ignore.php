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
        foreach ($rules as $rule) {
            $this->addRule($rule);
        }
    }

    /**
     * @param string $rule
     * @return $this
     */
    public function addRule(string $rule)
    {
        if (str::startsWith($rule, "./")) {
            $rule = str::sub($rule, 1);
        }

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