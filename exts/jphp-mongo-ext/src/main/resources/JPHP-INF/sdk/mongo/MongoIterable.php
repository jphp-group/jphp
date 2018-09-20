<?php

namespace mongo;

/**
 * Class MongoIterable
 * @package mongo
 */
class MongoIterable implements \Traversable
{
    private function __construct()
    {
    }

    /**
     * @return array
     */
    public function first(): array
    {
    }

    /**
     * @param int $size
     * @return MongoIterable
     */
    public function batchSize(int $size): MongoIterable
    {
    }

    /// ------------------ Only for List results ----------------------- ///

    public function skip(int $n): MongoIterable
    {
    }

    public function limit(int $n): MongoIterable
    {
    }

    public function projection(array $value): MongoIterable { }
    public function hint(array $value): MongoIterable { }
    public function max(array $value): MongoIterable { }
    public function min(array $value): MongoIterable { }
    public function filter(array $value): MongoIterable { }
    public function sort(array $value): MongoIterable { }
    public function comment(string $value): MongoIterable { }
    public function maxAwaitTime(int $millis): MongoIterable { }
    public function maxTime(int $millis): MongoIterable { }
    public function maxScan(int $c): MongoIterable { }
    public function returnKey(bool $value): MongoIterable { }
    public function partial(bool $value): MongoIterable { }
    public function snapshot(bool $value): MongoIterable { }
}