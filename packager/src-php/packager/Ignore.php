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
     * @var array
     */
    private $notRules = [];

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
        if ($not = str::startsWith($rule, '!')) {
            $rule = str::sub($rule, 1);
        }

        if (str::startsWith($rule, "./")) {
            $rule = str::sub($rule, 1);
        }

        if ($not) {
            $this->notRules[$rule] = $rule;
        } else {
            $this->rules[$rule] = $rule;
        }

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
        foreach ($this->notRules as $rule) {
            if (fs::match($this->base . $path, 'glob:' . $rule)) {
                return false;
            }
        }

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