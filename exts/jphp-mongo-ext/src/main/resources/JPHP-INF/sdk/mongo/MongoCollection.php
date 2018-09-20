<?php
namespace mongo;

/**
 * Class MongoCollection
 * @package mongo
 */
class MongoCollection
{
    private function __construct()
    {
    }

    /**
     * @param array $keys
     * @param array $options [optional]
     */
    public function createIndex(array $keys, array $options)
    {
    }

    /**
     * @param array $keys
     */
    public function dropIndex(array $keys)
    {
    }

    /**
     * @param string $name
     */
    public function dropIndexByName(string $name)
    {
    }

    /**
     *
     */
    public function dropIndexes()
    {
    }

    /**
     * @param array $filter
     * @return bool
     */
    public function deleteOne(array $filter): bool
    {
    }

    /**
     * @param array $filter
     * @return int count of deleted.
     */
    public function deleteMany(array $filter): int
    {
    }

    /**
     * @param array $filter
     * @param array $options [optional]
     * @return int
     */
    public function count(array $filter = [], array $options): int
    {
    }

    /**
     * @param array $filter
     * @return MongoIterable
     */
    public function find(array $filter = []): MongoIterable
    {
    }

    /**
     * @param array $filter
     * @return array|null
     */
    public function findOneAndDelete(array $filter): ?array
    {
    }

    /**
     * @param array $filter
     * @param array $update
     * @return array|null
     */
    public function findOneAndUpdate(array $filter, array $update): ?array
    {
    }

    /**
     * @param array $document
     * @return array
     */
    public function insertOne(array $document): array
    {
    }

    /**
     * @param array $documents
     */
    public function insertMany(array $documents): void
    {
    }

    /**
     * @param array $filter
     * @param array $document
     * @return array
     */
    public function updateOne(array $filter, array $document): array
    {
    }

    /**
     * @param array $filter
     * @param array $documents
     */
    public function updateMany(array $filter, array $documents): void
    {
    }

    /**
     * Drop collection.
     */
    public function drop()
    {
    }
}