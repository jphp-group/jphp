<?php
namespace packager;

use php\lang\Process;

class Java
{
    /**
     * @param array $args
     * @param string|null $directory
     * @return Process
     */
    public static function exec(array $args, string $directory = null)
    {
        $process = new Process(flow(['java'], $args)->toArray(), $directory);
        return $process;
    }
}