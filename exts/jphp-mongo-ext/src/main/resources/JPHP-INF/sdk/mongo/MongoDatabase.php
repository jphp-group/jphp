<?php
namespace mongo;

/**
 * Class MongoDatabase
 * @package mongo
 */
class MongoDatabase
{
    private function __construct()
    {
    }

    /**
     * @param string $name
     * @return MongoCollection
     */
    public function collection(string $name): MongoCollection
    {
    }
}