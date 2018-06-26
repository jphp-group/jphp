<?php
namespace mongo;

/**
 * Class MongoDatabase
 * @package mongo
 */
class MongoDatabase
{
    /**
     * @var string
     */
    public $name;

    private function __construct()
    {
    }

    /**
     * Drops this database.
     */
    public function drop()
    {
    }

    /**
     * @param string $name
     * @return MongoCollection
     */
    public function collection(string $name): MongoCollection
    {
    }

    /**
     * Finds all the collections in this database.
     *
     * @return MongoIterable
     */
    public function collections(): MongoIterable
    {
    }

    /**
     * Create a new collection with the given name.
     *
     * @param string $name
     */
    public function createCollection(string $name): void
    {
    }

    /**
     * Executes the given command in the context of the current database with a read preference of ReadPreference#primary().
     *
     * @param array $command
     * @return array
     */
    public function runCommand(array $command): array
    {
    }
}