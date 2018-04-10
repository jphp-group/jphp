<?php
namespace php\concurrent;

/**
 * Class Promise
 * @package php\concurrent
 *
 * @packages std, core
 */
class Promise
{
    /**
     * Promise constructor.
     * @param callable $executor (callable $resolve, callable $reject)
     */
    function __construct(callable $executor = null)
    {
    }

    private function makeFulfill($result = null)
    {
    }

    private function makeReject(\Throwable $error)
    {
    }

    /**
     * @param callable|null $onFulfilled
     * @param callable|null $onRejected
     * @return Promise
     */
    function then(callable $onFulfilled = null, callable $onRejected = null): Promise
    {
    }

    /**
     * @param callable|null $onRejected
     * @return Promise
     */
    function catch(?callable $onRejected = null): Promise
    {
        return $this->then(null, $onRejected);
    }

    /**
     * Stops execution until this promise is resolved.
     *
     * @return mixed
     * @throws \Throwable
     */
    function wait()
    {
    }

    /**
     * @param mixed $result
     * @return Promise
     */
    public static function resolve($result): Promise
    {
    }

    /**
     * @param \Throwable $error
     * @return Promise
     */
    public static function reject(\Throwable $error): Promise
    {
    }

    /**
     * @param Promise[]|iterable $promises
     * @return Promise
     */
    public static function race(iterable $promises): Promise
    {
    }

    /**
     * @param Promise[]|iterable $promises
     * @return Promise
     */
    public static function all(iterable $promises): Promise
    {
    }
}