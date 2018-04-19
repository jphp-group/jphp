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
}