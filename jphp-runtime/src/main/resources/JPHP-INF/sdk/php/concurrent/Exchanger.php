<?php
namespace php\concurrent;
use php\lang\InterruptedException;

/**
 * Class Exchanger
 * @package php\concurrent
 */
class Exchanger
{
    /**
     * @param mixed $value
     * @param int $timeout (optional) in millis
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public function exchange($value, $timeout) {}
}