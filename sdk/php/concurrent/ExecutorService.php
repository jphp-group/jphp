<?php
namespace php\concurrent;
use php\lang\Environment;

/**
 * Class ExecutorService
 * @package php\concurrent
 */
class ExecutorService {
    /**
     * internal
     */
    private function __construct() { }

    /**
     * @param callable $runnable
     * @param Environment $env
     */
    public function execute(callable $runnable, Environment $env = null) { }

    /**
     * @param int $max
     * @return ExecutorService
     */
    public static function newFixedThreadPool($max) { }

    /**
     * @return ExecutorService
     */
    public static function newCachedThreadPool() { }
}
