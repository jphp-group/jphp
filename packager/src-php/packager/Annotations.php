<?php
namespace packager;

use php\util\Regex;
use ReflectionClass;
use ReflectionFunctionAbstract;

/**
 * Class Annotations
 * @package framework\core
 */
class Annotations
{
    private function __construct()
    {
    }

    /**
     * @param string $annotationName
     * @param ReflectionClass $reflection
     * @param null $default
     * @return mixed
     */
    public static function getOfClass(string $annotationName, ReflectionClass $reflection, $default = null)
    {
        do {
            $var = static::get($annotationName, $reflection->getDocComment(), null);

            if (!$var) {
                $reflection = $reflection->getParentClass();

                if (!$reflection) {
                    return $default;
                }
            } else {
                break;
            }
        } while (true);

        if ($var && is_array($default) && !is_array($var)) {
            return [$var];
        }

        return $var;
    }

    /**
     * @param string $annotationName
     * @param ReflectionFunctionAbstract $reflection
     * @param null $default
     * @return mixed
     */
    public static function getOfMethod(string $annotationName, ReflectionFunctionAbstract $reflection, $default = null)
    {
        return static::get($annotationName, $reflection->getDocComment(), $default);
    }

    /**
     * @param string $annotationName
     * @param string $comment
     * @param mixed $default
     * @return mixed
     */
    public static function get(string $annotationName, string $comment, $default = null)
    {
        if (!$comment) {
            return $default;
        }

        $result = static::parse($comment)[$annotationName];

        if ($result && is_array($default) && !is_array($result)) {
            return [$result];
        }

        return $result ?: $default;
    }

    /**
     * @param ReflectionFunctionAbstract $reflection
     * @return array
     */
    public static function parseMethod(ReflectionFunctionAbstract $reflection): array
    {
        return static::parse($reflection->getDocComment());
    }

    /**
     * @param ReflectionClass $reflection
     * @return array
     */
    public static function parseClass(ReflectionClass $reflection): array
    {
        return static::parse($reflection->getDocComment());
    }

    /**
     * @param string $comment
     * @param callable|null $callback
     * @return array
     */
    public static function parse(string $comment, callable $callback = null): array
    {
        $regex = new Regex('\\@([a-z0-9\\-\\_]+)([ ]+(.+))?', 'im', $comment);

        $result = [];

        while ($regex->find()) {
            $groups = $regex->groups();

            $name = $groups[1];
            $value = $groups[3] ?: true;

            if ($callback) {
                if (!$callback($name, $value)) {
                    break;
                }
            }

            if ($result[$name]) {
                if (!is_array($result[$name])) {
                    $result[$name] = [$result[$name]];
                }

                $result[$name][] = $value;
            } else {
                $result[$name] = $value;
            }
        }

        return $result;
    }
}